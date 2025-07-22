import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { useRequireAuth } from '../../hooks/useRequireAuth';
import { FaUser, FaUserPlus, FaUserCheck, FaHeart, FaUsers, FaStar, FaMapMarkerAlt } from 'react-icons/fa';
import axios from 'axios';
import UserPosts from '../Profile/UserPosts';
import './Profile.css';

const Profile = () => {
    const { userId } = useParams();
    const { user: currentUser } = useRequireAuth();
    const navigate = useNavigate();
    const [profile, setProfile] = useState(null);
    const [isFollowing, setIsFollowing] = useState(false);
    const [error, setError] = useState('');
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const fetchProfile = async () => {
            try {
                setLoading(true);
                // If no userId is provided, use the current user's ID
                const targetUserId = userId || (currentUser && (currentUser.id || currentUser.sub));
                
                if (!targetUserId) {
                    setError('No user ID available');
                    return;
                }

                const response = await axios.get(`http://localhost:8081/api/users/${targetUserId}`, {
                    withCredentials: true
                });
                setProfile(response.data);
                
                // Check if current user is following this profile
                if (currentUser && targetUserId !== currentUser.id) {
                    const currentUserId = currentUser.id || currentUser.sub;
                    if (currentUserId) {
                        const followResponse = await axios.get(
                            `http://localhost:8081/api/users/${currentUserId}/following/${targetUserId}`,
                            { withCredentials: true }
                        );
                        setIsFollowing(followResponse.data);
                    }
                }
            } catch (error) {
                setError('Failed to fetch profile');
                console.error('Error fetching profile:', error);
            } finally {
                setLoading(false);
            }
        };

        if (currentUser) {
            fetchProfile();
        } else {
            navigate('/login');
        }
    }, [userId, currentUser, navigate]);

    const handleFollow = async () => {
        try {
            if (!currentUser) {
                setError('You must be logged in to follow users');
                return;
            }

            const currentUserId = currentUser.id || currentUser.sub;
            if (!currentUserId) {
                setError('Unable to determine user ID. Please log in again.');
                return;
            }

            const targetUserId = userId || currentUserId;

            if (isFollowing) {
                await axios.post(
                    `http://localhost:8081/api/users/${targetUserId}/unfollow`,
                    null,
                    {
                        params: { followerId: currentUserId },
                        withCredentials: true
                    }
                );
            } else {
                await axios.post(
                    `http://localhost:8081/api/users/${targetUserId}/follow`,
                    null,
                    {
                        params: { followerId: currentUserId },
                        withCredentials: true
                    }
                );
            }
            
            // Refresh profile data
            const response = await axios.get(`http://localhost:8081/api/users/${targetUserId}`, {
                withCredentials: true
            });
            setProfile(response.data);
            setIsFollowing(!isFollowing);
        } catch (error) {
            setError('Failed to update follow status: ' + (error.response?.data?.message || error.message));
            console.error('Error updating follow status:', error);
        }
    };

    if (loading) {
        return (
            <div className="content-container">
                <div className="loading-spinner">
                    <div className="spinner"></div>
                    <p>Loading profile...</p>
                </div>
            </div>
        );
    }

    if (!profile) {
        return (
            <div className="content-container">
                <div className="error-card">
                    <h2>Profile Not Found</h2>
                    <p>The user profile you're looking for doesn't exist.</p>
                </div>
            </div>
        );
    }

    // Get the current user's ID, handling both OAuth and manual login cases
    const currentUserId = currentUser?.id || currentUser?.sub;
    const targetUserId = userId || currentUserId;
    const isOwnProfile = currentUserId && parseInt(currentUserId) === parseInt(targetUserId);

    return (
        <div className="content-container">
            <div className="profile-page">
                {/* Profile Header */}
                <div className="profile-header-card">
                    <div className="profile-cover">
                        <div className="profile-cover-gradient"></div>
                    </div>
                    
                    <div className="profile-info">
                        <div className="profile-avatar-container">
                            {profile.profileImage ? (
                                <img 
                                    src={profile.profileImage.startsWith('data:') 
                                        ? profile.profileImage 
                                        : profile.profileImage.startsWith('http') 
                                            ? profile.profileImage 
                                            : `data:image/jpeg;base64,${profile.profileImage}`
                                    } 
                                    alt={profile.name} 
                                    className="profile-avatar"
                                    onError={(e) => {
                                        e.target.onerror = null;
                                        e.target.src = `https://ui-avatars.com/api/?name=${encodeURIComponent(profile.name)}&background=667eea&color=fff&size=120`;
                                    }}
                                />
                            ) : (
                                <div className="profile-avatar-placeholder">
                                    <FaUser />
                                </div>
                            )}
                            <div className="profile-status-indicator"></div>
                        </div>
                        
                        <div className="profile-details">
                            <h1 className="profile-name">{profile.name}</h1>
                            <p className="profile-email">
                                <span>@{profile.email.split('@')[0]}</span>
                            </p>
                            
                            <div className="profile-meta">
                                <div className="profile-location">
                                    <FaMapMarkerAlt />
                                    <span>Earth 🌍</span>
                                </div>
                                <div className="profile-joined">
                                    <span>Member since 2024</span>
                                </div>
                            </div>
                            
                            {currentUser && !isOwnProfile && (
                                <button 
                                    className={`follow-button ${isFollowing ? 'following' : ''}`}
                                    onClick={handleFollow}
                                >
                                    {isFollowing ? (
                                        <>
                                            <FaUserCheck />
                                            <span>Following</span>
                                        </>
                                    ) : (
                                        <>
                                            <FaUserPlus />
                                            <span>Follow</span>
                                        </>
                                    )}
                                </button>
                            )}
                        </div>
                    </div>
                </div>
                
                {/* Profile Stats */}
                <div className="profile-stats-grid">
                    <div className="stat-card">
                        <div className="stat-icon">
                            <FaUsers />
                        </div>
                        <div className="stat-content">
                            <div className="stat-value">{profile.followersCount || 0}</div>
                            <div className="stat-label">Followers</div>
                        </div>
                    </div>
                    
                    <div className="stat-card">
                        <div className="stat-icon">
                            <FaHeart />
                        </div>
                        <div className="stat-content">
                            <div className="stat-value">{profile.followingCount || 0}</div>
                            <div className="stat-label">Following</div>
                        </div>
                    </div>
                    
                    <div className="stat-card">
                        <div className="stat-icon">
                            <FaStar />
                        </div>
                        <div className="stat-content">
                            <div className="stat-value">{profile.postsCount || 0}</div>
                            <div className="stat-label">Posts</div>
                        </div>
                    </div>
                </div>

                {error && (
                    <div className="error-message">
                        <span>⚠️</span>
                        {error}
                    </div>
                )}

                {/* User Posts Section */}
                <div className="profile-posts-section">
                    <UserPosts userId={targetUserId} />
                </div>
            </div>
        </div>
    );
};

export default Profile;