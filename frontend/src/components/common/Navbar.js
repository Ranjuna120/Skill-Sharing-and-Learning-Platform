import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { useLocation } from 'react-router-dom';

const Navbar = () => {
  const location = useLocation();

  return (
    <nav className="navbar">
      <div className="nav-links">
        <Link to="/posts" className={location.pathname === '/posts' ? 'active' : ''}>
          Posts
        </Link>
        <Link to="/create-post" className={location.pathname === '/create-post' ? 'active' : ''}>
          Create Post
        </Link>
        <Link to="/plans" className={location.pathname === '/plans' ? 'active' : ''}>
          Learning Plans
        </Link>
        <Link to="/create-plan" className={location.pathname === '/create-plan' ? 'active' : ''}>
          Create Plan
        </Link>
        <Link to="/progress" className={location.pathname === '/progress' ? 'active' : ''}>
          Progress
        </Link>
        <Link to="/create-progress" className={location.pathname === '/create-progress' ? 'active' : ''}>
          Create Progress
        </Link>
        <Link to="/users" className={location.pathname === '/users' ? 'active' : ''}>
          Find Users
        </Link>
        <Link to="/profile" className={location.pathname === '/profile' ? 'active' : ''}>
          Profile
        </Link>
      </div>
    </nav>
  );
};

export default Navbar; 