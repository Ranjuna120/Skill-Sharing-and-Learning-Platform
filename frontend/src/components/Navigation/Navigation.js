import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../../contexts/AuthContext';
import { FaHome, FaPlus, FaUser, FaBell, FaBook, FaSignOutAlt, FaGraduationCap } from 'react-icons/fa';
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
        <nav className="modern-navigation">
            <div className="nav-container">
                <div className="nav-brand">
                    <Link to="/" className="brand-link">
                        <FaGraduationCap className="brand-icon" />
                        <span className="brand-text">Skill Sharing</span>
                    </Link>
                </div>
                
                <div className="nav-links">
                    {user ? (
                        <>
                            <Link to="/posts" className="nav-link" title="Posts">
                                <FaHome className="nav-icon" />
                                <span className="nav-text">Home</span>
                            </Link>
                            <Link to="/create-post" className="nav-link" title="Create Post">
                                <FaPlus className="nav-icon" />
                                <span className="nav-text">Create</span>
                            </Link>
                            <Link to="/plans" className="nav-link" title="Learning Plans">
                                <FaBook className="nav-icon" />
                                <span className="nav-text">Plans</span>
                            </Link>
                            <Link to="/notifications" className="nav-link" title="Notifications">
                                <FaBell className="nav-icon" />
                                <span className="nav-text">Notifications</span>
                            </Link>
                            <Link to={`/profile/${user.sub || user.id}`} className="nav-link profile-link" title="Profile">
                                <FaUser className="nav-icon" />
                                <span className="nav-text">Profile</span>
                            </Link>
                            <button onClick={handleLogout} className="logout-button" title="Logout">
                                <FaSignOutAlt className="nav-icon" />
                                <span className="nav-text">Logout</span>
                            </button>
                        </>
                    ) : (
                        <Link to="/login" className="nav-link login-link">
                            <FaUser className="nav-icon" />
                            <span className="nav-text">Login</span>
                        </Link>
                    )}
                </div>
            </div>
        </nav>
    );
};

export default Navigation; 