import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../../contexts/AuthContext';
import axios from 'axios';
import './Navigation.css';

const Navigation = () => {
    const { user, logout } = useAuth();
    const navigate = useNavigate();

    const handleLogout = async () => {
        try {
            await axios.post('http://localhost:8081/api/users/logout', {}, {
                withCredentials: true
            });
            logout();
            navigate('/login');
        } catch (error) {
            console.error('Error logging out:', error);
        }
    };

    return (
        <nav className="navigation">
            <div className="nav-brand">
                <Link to="/">Skill Sharing</Link>
            </div>
            <div className="nav-links">
                {user ? (
                    <>
                        <Link to="/posts">Posts</Link>
                        <Link to="/create-post">Create Post</Link>
                        <Link to={`/profile/${user.sub}`}>Profile</Link>
                        <button onClick={handleLogout} className="logout-button">
                            Logout
                        </button>
                    </>
                ) : (
                    <Link to="/login">Login</Link>
                )}
            </div>
        </nav>
    );
};

export default Navigation; 