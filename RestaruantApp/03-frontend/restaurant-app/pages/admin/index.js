import React from 'react';
import Link from 'next/link';

const AdminDashboard = () => {
  return (
    <div>
      <h1>Admin Dashboard</h1>
      <ul>
        <li>
          <Link href="/admin/orders">Orders</Link>
        </li>
        <li>
          <Link href="/admin/menu">Menu</Link>
        </li>
      </ul>
    </div>
  );
};

export default AdminDashboard;
