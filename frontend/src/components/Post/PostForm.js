import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../../contexts/AuthContext';
import axios from 'axios';
import './PostForm.css'; // Import the CSS file

const PostForm = () => {
  const navigate = useNavigate();
  const { user } = useAuth();
  const [formData, setFormData] = useState({
    title: '',
    description: '',
    image1: null,
    image2: null,
    image3: null,
    video: null
  });
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setLoading(true);

    if (!user) {
      setError('You must be logged in to create a post');
      setLoading(false);
      return;
    }

    // Extract and validate user ID
    const userId = user.sub || user.id;
    if (!userId) {
      setError('Unable to determine user ID. Please log in again.');
      setLoading(false);
      return;
    }

    try {
      const formDataToSend = new FormData();
      formDataToSend.append('title', formData.title);
      formDataToSend.append('description', formData.description);
      formDataToSend.append('userId', userId);

      // Add images as an array
      const images = [];
      if (formData.image1) images.push(formData.image1);
      if (formData.image2) images.push(formData.image2);
      if (formData.image3) images.push(formData.image3);

      // Append each image with the same parameter name
      images.forEach((image) => {
        formDataToSend.append('images', image);
      });

      // Add video if present
      if (formData.video) {
        formDataToSend.append('video', formData.video);
      }

      console.log('Submitting form data:', {
        title: formData.title,
        description: formData.description,
        userId: userId,
        numberOfImages: images.length,
        hasVideo: !!formData.video
      });

      const response = await axios.post('http://localhost:8081/api/posts', formDataToSend, {
        withCredentials: true,
        headers: {
          'Content-Type': 'multipart/form-data',
        },
      });

      if (response.data) {
        console.log('Post created successfully:', response.data);
        navigate('/posts');
      } else {
        setError('Failed to create post. No response data received.');
      }
    } catch (error) {
      console.error('Error creating post:', error.response?.data || error);
      const errorMessage = error.response?.data?.message || 
                         error.response?.data?.error || 
                         error.message ||
                         'Failed to create post. Please try again.';
      setError(errorMessage);
    } finally {
      setLoading(false);
    }
  };

  const handleChange = (e) => {
    const { name, value, files } = e.target;
    if (files) {
      setFormData(prev => ({
        ...prev,
        [name]: files[0]
      }));
    } else {
      setFormData(prev => ({
        ...prev,
        [name]: value
      }));
    }
  };

  if (!user) {
    return (
      <div className="page-container">
        <div className="card card-error">
          <div className="card-content text-center">
            <h2>Authentication Required</h2>
            <p>Please log in to create a post.</p>
            <button className="btn btn-primary" onClick={() => navigate('/login')}>
              Login
            </button>
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="content-container">
      <div className="form-modern animate-fade-in">
        <div className="form-header">
          <h1 className="post-form-title-modern">Share Your Knowledge</h1>
          <p className="post-form-subtitle">Create a post to share skills, ask questions, or connect with others</p>
        </div>
        
        <form onSubmit={handleSubmit}>
          <div className="form-group-modern">
            <label className="form-label-modern" htmlFor="title">Post Title</label>
            <div className="input-with-icon">
              <input
                type="text"
                id="title"
                name="title"
                value={formData.title}
                onChange={handleChange}
                className="form-input-modern"
                placeholder="Enter an engaging title for your post"
                required
              />
              <span className="input-icon">📝</span>
            </div>
          </div>

          <div className="form-group-modern">
            <label className="form-label-modern" htmlFor="description">Description</label>
            <textarea
              id="description"
              name="description"
              value={formData.description}
              onChange={handleChange}
              className="form-input-modern form-textarea-modern"
              placeholder="Share your knowledge, ask questions, or provide detailed insights..."
              required
            />
          </div>

          <div className="grid grid-cols-3 gap-4">
            <div className="form-group-modern">
              <label className="form-label-modern" htmlFor="image1">Image 1</label>
              <div className="file-input-modern">
                <input
                  type="file"
                  id="image1"
                  name="image1"
                  onChange={handleChange}
                  accept="image/*"
                />
                <div className={`file-input-label ${formData.image1 ? 'has-file' : ''}`}>
                  <span>{formData.image1 ? '✓ Image 1 Selected' : '📷 Choose Image'}</span>
                </div>
              </div>
            </div>

            <div className="form-group-modern">
              <label className="form-label-modern" htmlFor="image2">Image 2</label>
              <div className="file-input-modern">
                <input
                  type="file"
                  id="image2"
                  name="image2"
                  onChange={handleChange}
                  accept="image/*"
                />
                <div className={`file-input-label ${formData.image2 ? 'has-file' : ''}`}>
                  <span>{formData.image2 ? '✓ Image 2 Selected' : '📷 Choose Image'}</span>
                </div>
              </div>
            </div>

            <div className="form-group-modern">
              <label className="form-label-modern" htmlFor="image3">Image 3</label>
              <div className="file-input-modern">
                <input
                  type="file"
                  id="image3"
                  name="image3"
                  onChange={handleChange}
                  accept="image/*"
                />
                <div className={`file-input-label ${formData.image3 ? 'has-file' : ''}`}>
                  <span>{formData.image3 ? '✓ Image 3 Selected' : '📷 Choose Image'}</span>
                </div>
              </div>
            </div>
          </div>

          <div className="form-group-modern">
            <label className="form-label-modern" htmlFor="video">Video (Optional)</label>
            <div className="file-input-modern">
              <input
                type="file"
                id="video"
                name="video"
                onChange={handleChange}
                accept="video/*"
              />
              <div className={`file-input-label ${formData.video ? 'has-file' : ''}`}>
                <span>{formData.video ? '✓ Video Selected' : '🎥 Choose Video'}</span>
              </div>
            </div>
          </div>

          {error && (
            <div className="form-error">
              ⚠️ {error}
            </div>
          )}
          
          <div className="form-actions-modern center">
            <button 
              type="button" 
              className="btn btn-outline" 
              onClick={() => navigate('/posts')}
              disabled={loading}
            >
              Cancel
            </button>
            <button 
              type="submit" 
              className={`btn btn-primary btn-lg ${loading ? 'loading' : ''}`}
              disabled={loading}
            >
              {loading ? (
                <>
                  <div className="spinner-small"></div>
                  Creating...
                </>
              ) : (
                <>
                  <span>🚀</span>
                  Create Post
                </>
              )}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default PostForm;