const API_BASE = 'http://localhost:8080';

async function loadProducts() {
    const res = await fetch(`${API_BASE}/products`);
    return await res.json();
}

async function placeOrder(productId, quantity, customerName) {
    const res = await fetch(`${API_BASE}/orders`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ productId, quantity, customerName })
    });
    return await res.json();
}

async function payForOrder(payLink) {
    const res = await fetch(payLink, { method: 'POST' });
    return await res.json();
}