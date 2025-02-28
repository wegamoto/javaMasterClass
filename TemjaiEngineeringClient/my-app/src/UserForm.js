import React, { useState } from "react";
import axios from "axios";


const UserForm = () => {
  const [firstname, setFirstname] = useState("");
  const [lastname, setLastname] = useState("");

  const handleSubmit = async (e) => {
    e.preventDefault();
    const userData = { firstname, lastname };
    
    try {
      const response = await axios.post("/api/users", userData);
      alert("User added successfully!");
      setFirstname("");
      setLastname("");
    } catch (error) {
      console.error("Error saving user", error);
      alert("Error saving user");
    } 
  };

  return (
    <div>
      <h2>Add User</h2>
      <form onSubmit={handleSubmit}>
        <label>Firstname:</label>
        <input type="text" value={firstname} onChange={(e) => setFirstname(e.target.value)} required />

        <label>Lastname:</label>
        <input type="text" value={lastname} onChange={(e) => setLastname(e.target.value)} required />

        <button type="submit">Submit</button>
      </form>
    </div>
  );
};

export default UserForm;
