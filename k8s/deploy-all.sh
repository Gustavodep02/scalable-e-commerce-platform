#!/bin/bash
set -e

GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m'

ROOT_DIR="$(cd "$(dirname "$0")/.." && pwd)"
K8S_DIR="$ROOT_DIR/k8s"

log()  { echo -e "${GREEN}[✔] $1${NC}"; }
warn() { echo -e "${YELLOW}[!] $1${NC}"; }
err()  { echo -e "${RED}[✘] $1${NC}"; exit 1; }

echo ""
echo "========================================="
echo "  Scalable E-Commerce Platform - Deploy  "
echo "========================================="
echo ""

# =============================================
# 1. Check prerequisites
# =============================================
command -v kubectl >/dev/null 2>&1 || err "kubectl not found"
command -v kind >/dev/null 2>&1    || err "kind not found"
command -v docker >/dev/null 2>&1  || err "docker not found"
log "Prerequisites OK"

# =============================================
# 2. Create Kind cluster if not exists
# =============================================
if kind get clusters 2>/dev/null | grep -q "kind"; then
  warn "Kind cluster already exists, skipping creation..."
else
  log "Creating Kind cluster..."
  kind create cluster --config "$K8S_DIR/config.yaml"
fi
log "Cluster ready"

# =============================================
# 3. Build Docker images
# =============================================
echo ""
log "Building Docker images..."

SERVICES=("user-service" "product-service" "cart-service" "order-service" "payment-service" "notification-service" "eureka" "api-gateway")

for svc in "${SERVICES[@]}"; do
  echo -n "  Building $svc... "
  docker build -t "$svc:latest" "$ROOT_DIR/$svc" -q > /dev/null 2>&1
  echo "OK"
done
log "All images built"

# =============================================
# 4. Load images into Kind
# =============================================
echo ""
log "Loading images into Kind cluster..."

for svc in "${SERVICES[@]}"; do
  echo -n "  Loading $svc... "
  kind load docker-image "$svc:latest" > /dev/null 2>&1
  echo "OK"
done
log "All images loaded"

# =============================================
# 5. Apply ConfigMap and Secret
# =============================================
echo ""
log "Applying ConfigMap and Secret..."

[ ! -f "$K8S_DIR/secret.yaml" ] && err "k8s/secret.yaml not found! Run: cp k8s/secret.yaml.example k8s/secret.yaml"

kubectl apply -f "$K8S_DIR/configmap.yaml"
kubectl apply -f "$K8S_DIR/secret.yaml"

# =============================================
# 6. Infrastructure
# =============================================
echo ""
log "Deploying infrastructure..."
kubectl create configmap prometheus-config --from-file=prometheus.yml="$ROOT_DIR/prometheus/prometheus.yml" --dry-run=client -o yaml | kubectl apply -f -
kubectl apply -f "$K8S_DIR/infrastructure/"

# =============================================
# 7. Databases
# =============================================
echo ""
log "Deploying databases..."
kubectl apply -f "$K8S_DIR/db/"

echo -n "  Waiting for databases..."
for db in user-db product-db cart-db order-db payment-db notification-db; do
  kubectl wait --for=condition=ready pod -l app=$db --timeout=120s > /dev/null 2>&1 || true
done
echo " OK"

# =============================================
# 8. Eureka (Service Discovery)
# =============================================
echo ""
log "Deploying Eureka..."
kubectl apply -f "$K8S_DIR/eureka/"

echo -n "  Waiting for Eureka..."
kubectl wait --for=condition=ready pod -l app=eureka --timeout=180s > /dev/null 2>&1 || warn "Eureka took too long, continuing..."
echo " OK"

# =============================================
# 9. Microservices
# =============================================
echo ""
log "Deploying microservices..."
kubectl apply -f "$K8S_DIR/user-service/"
kubectl apply -f "$K8S_DIR/product-service/"
kubectl apply -f "$K8S_DIR/cart-service/"
kubectl apply -f "$K8S_DIR/order-service/"
kubectl apply -f "$K8S_DIR/payment-service/"
kubectl apply -f "$K8S_DIR/notification-service/"

# =============================================
# 10. API Gateway
# =============================================
echo ""
log "Deploying API Gateway..."
kubectl apply -f "$K8S_DIR/api-gateway/"

# =============================================
# 11. Summary
# =============================================
echo ""
echo "========================================="
echo "  Deploy complete!                       "
echo "========================================="
echo ""
kubectl get pods
echo ""
kubectl get svc
echo ""
echo "-----------------------------------------"
echo "  Useful commands:                        "
echo "-----------------------------------------"
echo "  Watch pods:        kubectl get pods -w"
echo "  Service logs:      kubectl logs -f deployment/<service>"
echo "  API Gateway:       kubectl port-forward svc/api-gateway 8080:80"
echo "  Eureka dashboard:  kubectl port-forward svc/eureka 8761:8761"
echo "  Grafana:           kubectl port-forward svc/grafana 3000:3000"
echo "  Kibana:            kubectl port-forward svc/kibana 5601:5601"
echo ""

