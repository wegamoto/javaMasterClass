export default function handler(req, res) {
    const orders = [
      { id: 1, status: 'pending', totalPrice: 150 },
      { id: 2, status: 'completed', totalPrice: 200 },
    ];
  
    res.status(200).json(orders);
  }
  