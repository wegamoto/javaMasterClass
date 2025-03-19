import React, { useState, useEffect } from 'react';

const AdminOrdersPage = () => {
  const [orders, setOrders] = useState([]);

  useEffect(() => {
    fetch('/api/admin/orders')
      .then(response => response.json())
      .then(data => setOrders(data));
  }, []);

  return (
    <div>
      <h1>All Orders</h1>
      <div>
        {orders.map(order => (
          <div key={order.id}>
            <h2>Order {order.id}</h2>
            <p>Status: {order.status}</p>
            <p>Total: ${order.totalPrice}</p>
            <button>Update Status</button>
          </div>
        ))}
      </div>
    </div>
  );
};

export default AdminOrdersPage;
