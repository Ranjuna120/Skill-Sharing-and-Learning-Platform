const handleFollowToggle = async () => {
  try {
    if (!user) {
      setError('You must be logged in to follow/unfollow users');
      return;
    }

    const followerId = user.id || user.sub;
    if (!followerId) {
      setError('Unable to determine user ID. Please log in again.');
      return;
    }

    if (isFollowing) {
      const response = await unfollowUser(userId, followerId);
      if (response && response.followersCount !== undefined) {
        setUserData(prev => ({
          ...prev,
          followersCount: response.followersCount,
          isFollowing: false
        }));
      }
    } else {
      const response = await followUser(userId, followerId);
      if (response && response.followersCount !== undefined) {
        setUserData(prev => ({
          ...prev,
          followersCount: response.followersCount,
          isFollowing: true
        }));
      }
    }
  } catch (error) {
    console.error('Error updating follow status:', error);
    setError(`Failed to update follow status: ${error.message}`);
  }
}; 