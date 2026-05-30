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

function connectWebSocket(customerName, onStatusChange) {
    const socket = new SockJS('http://localhost:8085/notification-websocket');
    const client = Stomp.over(socket);
    client.debug = null;

    client.connect({}, function () {
        console.log('WebSocket połączony, subskrybuję...');
        client.subscribe(`/topic/orders/${customerName}`, function (msg) {
            const event = JSON.parse(msg.body);
            console.log('Otrzymano event:', event);
            onStatusChange(event);
        });
    });

    return client;
}