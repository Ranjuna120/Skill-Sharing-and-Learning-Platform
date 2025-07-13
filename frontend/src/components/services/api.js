import axios from 'axios';

const API_URL = 'http://localhost:8081/api';

// Create axios instance with base configuration
const api = axios.create({
  baseURL: API_URL,
  withCredentials: true, // For CORS with credentials
  headers: {
    'Content-Type': 'application/json',
    'Accept': 'application/json'
  },
});

// Add request interceptor to handle authentication
api.interceptors.request.use(
  (config) => {
    // Add any auth headers if needed
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Add response interceptor to handle auth errors
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      // Redirect to login or handle auth error
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

// Plans API calls
export const getPlans = () => {
  return api.get('/plans')
    .then(response => {
      // Ensure response.data is an array; fallback to empty array if not
      const data = Array.isArray(response.data) ? response.data : [];
      return { data }; // Return as { data: [...] } to match PlanList expectation
    })
    .catch(error => {
      console.error('Failed to fetch plans:', error.response?.data || error.message);
      return { data: [] }; // Return fallback structure on error
    });
};

// ... (rest of your API functions remain unchanged unless you want similar fixes applied)

export const createPlan = (plan, userId) => {
  return api.post('/plans', plan, { 
    params: { userId },
    headers: { 'Content-Type': 'application/json' },
  })
    .then(response => response.data)
    .catch(error => {
      throw new Error('Failed to create plan: ' + (error.response?.data?.message || error.message));
    });
};

// Posts API calls
export const getPosts = () => {
  return api.get('/posts')
    .then(response => {
      // Ensure response.data is an array; fallback to empty array if not
      const data = Array.isArray(response.data) ? response.data : [];
      return data; // Return the array directly
    })
    .catch(error => {
      console.error('Failed to fetch posts:', error.response?.data || error.message);
      return []; // Return empty array on error
    });
};

export const createPost = (formData) => {
  return api.post('/posts', formData, {
    headers: { 
      'Content-Type': 'multipart/form-data',
    },
  })
    .then(response => response.data)
    .catch(error => {
      throw new Error('Failed to create post: ' + (error.response?.data?.message || error.message));
    });
};

export const deletePost = (postId, userId) => {
  return api.delete(`/posts/${postId}`, {
    params: { userId }
  })
    .then(response => response.data)
    .catch(error => {
      throw new Error('Failed to delete post: ' + (error.response?.data?.message || error.message));
    });
};

// Comments API calls
export const getComments = (postId) => {
  return api.get(`/comments/post/${postId}`)
    .then(response => response.data)
    .catch(error => {
      throw new Error('Failed to fetch comments: ' + (error.response?.data?.message || error.message));
    });
};

export const addComment = (postId, comment, userId) => {
  return api.post(`/comments/post/${postId}`, { content: comment }, { 
    params: { userId },
    headers: { 'Content-Type': 'application/json' },
  })
    .then(response => response.data)
    .catch(error => {
      throw new Error('Failed to add comment: ' + (error.response?.data?.message || error.message));
    });
};

export const updateComment = (commentId, content) => {
  return api.put(`/comments/${commentId}`, { content }, {
    headers: { 'Content-Type': 'application/json' },
  })
    .then(response => response.data)
    .catch(error => {
      throw new Error('Failed to update comment: ' + (error.response?.data?.message || error.message));
    });
};

export const deleteComment = (commentId) => {
  return api.delete(`/comments/${commentId}`)
    .then(response => response.data)
    .catch(error => {
      throw new Error('Failed to delete comment: ' + (error.response?.data?.message || error.message));
    });
};

// Progress API calls
export const getProgress = () => {
  return api.get('/progress')
    .then(response => {
      // Ensure response.data is an array; fallback to empty array if not
      const data = Array.isArray(response.data) ? response.data : [];
      return { data }; // Return as { data: [...] } to match ProgressList expectation
    })
    .catch(error => {
      console.error('Failed to fetch progress:', error.response?.data || error.message);
      return { data: [] }; // Return fallback structure on error
    });
};

export const createProgress = (progress, userId) => {
  return api.post('/progress', progress, { 
    params: { userId },
    headers: { 'Content-Type': 'application/json' },
  })
    .then(response => response.data)
    .catch(error => {
      throw new Error('Failed to create progress: ' + (error.response?.data?.message || error.message));
    });
};

export const updateProgress = (progressId, progress, userId) => {
  return api.put(`/progress/${progressId}`, 
    { 
      content: progress.content,
      templateType: progress.templateType
    }, 
    {
      params: { userId },
      headers: {
        'Content-Type': 'application/json'
      }
    }
  )
    .then(response => response.data)
    .catch(error => {
      console.error('Error updating progress:', error);
      throw new Error('Failed to update progress: ' + (error.response?.data?.message || error.message));
    });
};

// User API calls
export const getUser = (id) => {
  return api.get(`/users/${id}`)
    .then(response => response.data)
    .catch(error => {
      throw new Error('Failed to fetch user: ' + (error.response?.data?.message || error.message));
    });
};

export const followUser = (userId, followedUserId) => {
  return api.post('/users/follow', null, { 
    params: { userId, followedUserId },
  })
    .then(response => response.data)
    .catch(error => {
      throw new Error('Failed to follow user: ' + (error.response?.data?.message || error.message));
    });
};

export const searchUsers = async (query) => {
  try {
    const response = await api.get(`/users/search?query=${encodeURIComponent(query)}`);
    return response.data;
  } catch (error) {
    console.error('Error searching users:', error);
    return [];
  }
};

export const isFollowing = async (userId, followedUserId) => {
  try {
    const response = await api.get(`/users/${userId}/following/${followedUserId}`);
    return response.data;
  } catch (error) {
    console.error('Error checking follow status:', error);
    return false;
  }
};

export const deleteLearningPlan = (planId, userId) => {
  return api.delete(`/plans/${planId}`, {
    params: { userId }
  })
    .then(response => response.data)
    .catch(error => {
      throw new Error('Failed to delete learning plan: ' + (error.response?.data?.message || error.message));
    });
};

export const deleteProgressUpdate = (progressId, userId) => {
  return api.delete(`/progress/${progressId}`, {
    params: { userId },
    withCredentials: true
  })
    .then(response => response.data)
    .catch(error => {
      throw new Error('Failed to delete progress update: ' + (error.response?.data?.message || error.message));
    });
};

// Post interactions
export const likePost = async (postId, userId) => {
  try {
    const response = await api.post(`/posts/${postId}/like`, null, {
      params: { userId }
    });
    return response.data;
  } catch (error) {
    throw new Error('Failed to like post: ' + (error.response?.data?.message || error.message));
  }
};

export const favoritePost = async (postId, userId) => {
  try {
    const response = await api.post(`/posts/${postId}/favorite`, null, {
      params: { userId }
    });
    return response.data;
  } catch (error) {
    throw new Error('Failed to favorite post: ' + (error.response?.data?.message || error.message));
  }
};

export const unfollowUser = async (userId, followerId) => {
  try {
    const response = await api.post('/users/unfollow', null, {
      params: { userId, followerId }
    });
    return response.data;
  } catch (error) {
    console.error('Error unfollowing user:', error);
    throw new Error(error.response?.data?.message || 'Failed to unfollow user');
  }
};

export default api;