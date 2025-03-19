import React, { useState } from 'react';

const OrderPage = () => {
  const [orderItems, setOrderItems] = useState([]);
  const [totalPrice, setTotalPrice] = useState(0);

  const addToOrder = (item) => {
    setOrderItems([...orderItems, item]);
    setTotalPrice(totalPrice + item.price);
  };

  const submitOrder = () => {
    // Handle order submission
    console.log('Order submitted', orderItems);
  };

  return (
    <div>
      <h1>Your Order</h1>
      <div>
        {orderItems.map((item, index) => (
          <div key={index}>
            <h2>{item.name}</h2>
            <p>${item.price}</p>
          </div>
        ))}
        <h3>Total: ${totalPrice}</h3>
      </div>
      <button onClick={submitOrder}>Submit Order</button>
    </div>
  );
};

export default OrderPage;
