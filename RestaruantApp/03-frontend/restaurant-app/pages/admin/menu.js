import React, { useState } from "react";

const AdminMenuPage = () => {
  const [menuItems, setMenuItems] = useState([]);
  const [newItem, setNewItem] = useState({ name: "", price: "", description: "" });

  const handleAddItem = () => {
    setMenuItems([...menuItems, newItem]);
    setNewItem({ name: "", price: "", description: "" });
  };

  return (
    <div className="max-w-3xl mx-auto p-6">
      <h1 className="text-2xl font-bold mb-4 text-center">Menu Management</h1>

      {/* Input Form */}
      <div className="bg-white shadow-md rounded-lg p-6 mb-6">
        <h2 className="text-lg font-semibold mb-4">Add New Item</h2>
        <div className="space-y-3">
          <input
            type="text"
            placeholder="Item Name"
            value={newItem.name}
            onChange={(e) => setNewItem({ ...newItem, name: e.target.value })}
            className="w-full px-4 py-2 border rounded-md focus:ring-2 focus:ring-blue-400 focus:outline-none"
          />
          <input
            type="number"
            placeholder="Price"
            value={newItem.price}
            onChange={(e) => setNewItem({ ...newItem, price: e.target.value })}
            className="w-full px-4 py-2 border rounded-md focus:ring-2 focus:ring-blue-400 focus:outline-none"
          />
          <input
            type="text"
            placeholder="Description"
            value={newItem.description}
            onChange={(e) => setNewItem({ ...newItem, description: e.target.value })}
            className="w-full px-4 py-2 border rounded-md focus:ring-2 focus:ring-blue-400 focus:outline-none"
          />
          <button
            onClick={handleAddItem}
            className="w-full bg-blue-500 text-white py-2 rounded-md hover:bg-blue-600 transition duration-200"
          >
            Add Item
          </button>
        </div>
      </div>

      {/* Menu List */}
      <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
        {menuItems.map((item, index) => (
          <div key={index} className="bg-white shadow-md rounded-lg p-4">
            <h3 className="text-lg font-bold">{item.name}</h3>
            <p className="text-gray-700">${item.price}</p>
            <p className="text-gray-600">{item.description}</p>
          </div>
        ))}
      </div>
    </div>
  );
};

export default AdminMenuPage;
