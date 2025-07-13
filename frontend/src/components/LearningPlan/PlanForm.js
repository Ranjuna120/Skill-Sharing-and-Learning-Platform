import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../../contexts/AuthContext';
import axios from 'axios';
import './PlanForm.css';

const PlanForm = () => {
  const navigate = useNavigate();
  const { user } = useAuth();
  const [formData, setFormData] = useState({
    title: '',
    topics: '',
    resources: '',
    timeline: ''
  });
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setLoading(true);

    if (!user) {
      setError('You must be logged in to create a learning plan');
      setLoading(false);
      return;
    }

    const userId = user.sub || user.id;
    if (!userId) {
      setError('Unable to determine user ID. Please log in again.');
      setLoading(false);
      return;
    }

    try {
      await axios.post('http://localhost:8081/api/plans', 
        { ...formData },
        {
          params: { userId },
          withCredentials: true,
          headers: {
            'Content-Type': 'application/json'
          }
        }
      );
      navigate('/plans');
    } catch (error) {
      setError('Failed to create learning plan: ' + (error.response?.data?.message || error.message));
      console.error('Error creating learning plan:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value
    });
  };

  if (!user) {
    return <div className="error-message">Please log in to create a learning plan.</div>;
  }

  return (
    <div className="plan-form-container">
      <h1>Create Learning Plan</h1>
      {error && <div className="error-message">{error}</div>}
      <form onSubmit={handleSubmit} className="plan-form">
        <div className="form-group">
          <label htmlFor="title">Title</label>
          <input
            type="text"
            id="title"
            name="title"
            value={formData.title}
            onChange={handleChange}
            required
            placeholder="Enter plan title"
          />
        </div>
        <div className="form-group">
          <label htmlFor="topics">Topics</label>
          <textarea
            id="topics"
            name="topics"
            value={formData.topics}
            onChange={handleChange}
            required
            placeholder="List the topics you want to learn"
          />
        </div>
        <div className="form-group">
          <label htmlFor="resources">Resources</label>
          <textarea
            id="resources"
            name="resources"
            value={formData.resources}
            onChange={handleChange}
            required
            placeholder="List the resources you'll use (books, courses, etc.)"
          />
        </div>
        <div className="form-group">
          <label htmlFor="timeline">Timeline</label>
          <input
            type="text"
            id="timeline"
            name="timeline"
            value={formData.timeline}
            onChange={handleChange}
            required
            placeholder="e.g., 3 months, 6 weeks"
          />
        </div>
        <button type="submit" className="submit-btn" disabled={loading}>
          {loading ? 'Creating...' : 'Create Plan'}
        </button>
      </form>
    </div>
  );
};

export default PlanForm;