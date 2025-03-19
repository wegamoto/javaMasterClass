export default function handler(req, res) {
    const menu = [
      { id: 1, name: 'Pad Thai', price: 100, description: 'Fried noodle with shrimp' },
      { id: 2, name: 'Tom Yum', price: 50, description: 'Spicy shrimp soup' },
    ];
  
    res.status(200).json(menu);
  }
  