import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import { useRequireAuth } from '../../hooks/useRequireAuth';
import axios from 'axios';
import UserPosts from './UserPosts';
import './UserProfile.css';

const UserProfile = () => {
    const { id } = useParams();
    const { user: currentUser } = useRequireAuth();
    const [profile, setProfile] = useState(null);
    const [isFollowing, setIsFollowing] = useState(false);
    const [error, setError] = useState('');

    useEffect(() => {
        const fetchProfile = async () => {
            try {
                const response = await axios.get(`http://localhost:8081/api/users/${id}`, {
                    withCredentials: true
                });
                setProfile(response.data);
                
                // Check if current user is following this profile
                if (currentUser) {
                    const userId = currentUser.sub || currentUser.id;
                    if (userId) {
                        const followResponse = await axios.get(
                            `http://localhost:8081/api/users/${userId}/following/${id}`,
                            { withCredentials: true }
                        );
                        setIsFollowing(followResponse.data);
                    }
                }
            } catch (error) {
                setError('Failed to fetch profile');
                console.error('Error fetching profile:', error);
            }
        };

        fetchProfile();
    }, [id, currentUser]);

    const handleFollow = async () => {
        try {
            if (!currentUser) {
                setError('You must be logged in to follow users');
                return;
            }

            const userId = currentUser.sub || currentUser.id;
            if (!userId) {
                setError('Unable to determine user ID. Please log in again.');
                return;
            }

            if (isFollowing) {
                await axios.post(
                    `http://localhost:8081/api/users/${id}/unfollow`,
                    null,
                    {
                        params: { followerId: userId },
                        withCredentials: true
                    }
                );
            } else {
                await axios.post(
                    `http://localhost:8081/api/users/${id}/follow`,
                    null,
                    {
                        params: { followerId: userId },
                        withCredentials: true
                    }
                );
            }
            
            // Refresh profile data
            const response = await axios.get(`http://localhost:8081/api/users/${id}`, {
                withCredentials: true
            });
            setProfile(response.data);
            setIsFollowing(!isFollowing);
        } catch (error) {
            setError('Failed to update follow status: ' + (error.response?.data?.message || error.message));
            console.error('Error updating follow status:', error);
        }
    };

    if (!profile) {
        return <div className="loading">Loading...</div>;
    }

    return (
        <div className="profile-container">
            <div className="profile-header">
                <img 
                    src={profile.profileImage || '/default-avatar.png'} 
                    alt={profile.name} 
                    className="profile-image"
                />
                <h1 className="profile-name">{profile.name}</h1>
                <p className="profile-email">{profile.email}</p>
                
                {currentUser && currentUser.id !== parseInt(id) && (
                    <button 
                        className={`follow-button ${isFollowing ? 'following' : ''}`}
                        onClick={handleFollow}
                    >
                        {isFollowing ? 'Following' : 'Follow'}
                    </button>
                )}
            </div>
            
            <div className="profile-stats">
                <div className="stat-item">
                    <span className="stat-value">{profile.followersCount}</span>
                    <span className="stat-label">Followers</span>
                </div>
                <div className="stat-item">
                    <span className="stat-value">{profile.followingCount}</span>
                    <span className="stat-label">Following</span>
                </div>
            </div>

            {error && <div className="error-message">{error}</div>}

            {/* Only show UserPosts if viewing own profile */}
            {currentUser && currentUser.id === parseInt(id) && (
                <UserPosts userId={id} />
            )}
        </div>
    );
};

export default UserProfile; 