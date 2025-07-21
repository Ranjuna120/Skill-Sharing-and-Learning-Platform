import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { FaPlus, FaUser, FaEdit, FaTrash, FaBook, FaClock, FaGraduationCap, FaBullseye } from 'react-icons/fa';
import { getPlans, deleteLearningPlan } from '../services/api';
import { useAuth } from '../../contexts/AuthContext';
import axios from 'axios';
import './PlanList.css';

const PlanList = () => {
  const [plans, setPlans] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const { user } = useAuth();
  const [editingPlan, setEditingPlan] = useState(null);
  const [editForm, setEditForm] = useState({
    title: '',
    topics: '',
    resources: '',
    timeline: ''
  });

  useEffect(() => {
    const fetchPlans = async () => {
      try {
        setLoading(true);
        const response = await getPlans();
        const plansWithDetails = response.data.map(plan => ({
          ...plan,
          userName: plan.userName || 'Anonymous',
          userProfileImage: plan.userProfileImage || null
        }));
        setPlans(plansWithDetails);
      } catch (err) {
        setError('Failed to load plans: ' + err.message);
        console.error('Error fetching plans:', err);
        setPlans([]);
      } finally {
        setLoading(false);
      }
    };

    fetchPlans();
  }, []);

  const handleDeletePlan = async (planId) => {
    if (!window.confirm('Are you sure you want to delete this learning plan?')) {
      return;
    }

    try {
      await deleteLearningPlan(planId, user.id);
      setPlans(plans.filter(plan => plan.id !== planId));
    } catch (err) {
      setError('Failed to delete learning plan: ' + err.message);
    }
  };

  const handleEditPlan = async (planId) => {
    try {
      const response = await axios.put(`http://localhost:8081/api/plans/${planId}`, editForm, {
        withCredentials: true
      });
      
      setPlans(plans.map(plan => 
        plan.id === planId ? response.data : plan
      ));
      setEditingPlan(null);
      setEditForm({ title: '', topics: '', resources: '', timeline: '' });
    } catch (err) {
      setError('Failed to update learning plan: ' + err.message);
      console.error('Error updating learning plan:', err);
    }
  };

  const startEditing = (plan) => {
    setEditingPlan(plan.id);
    setEditForm({
      title: plan.title,
      topics: plan.topics,
      resources: plan.resources,
      timeline: plan.timeline
    });
  };

  const cancelEditing = () => {
    setEditingPlan(null);
    setEditForm({ title: '', topics: '', resources: '', timeline: '' });
  };

  if (loading) {
    return (
      <div className="plans-page">
        <div className="plans-page-container">
          <div className="loading-spinner">
            <div className="spinner"></div>
            <p>Loading learning plans...</p>
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="plans-page">
      <div className="plans-page-container">
        <div className="page-header">
          <div className="header-content">
            <div className="header-text">
              <h1 className="Plan-page-title">
                <FaGraduationCap className="title-icon" />
                Learning Plans
              </h1>
              <p className="Plan-page-subtitle">Discover structured learning paths and create your own</p>
            </div>
            <Link to="/create-plan" className="btn btn-primary">
              <FaPlus />
              <span>Create Plan</span>
            </Link>
          </div>
        </div>

        {error && (
          <div className="error-message">
            <span>⚠️</span>
            {error}
          </div>
        )}

        <div className="plans-grid">
          {plans.length === 0 ? (
            <div className="no-plans-card">
              <div className="no-plans-icon">
                <FaBook />
              </div>
              <h3>No Learning Plans Yet</h3>
              <p>Be the first to create a learning plan and share your knowledge with the community!</p>
              <Link to="/create-plan" className="btn btn-primary">
                <FaPlus />
                Create Your First Plan
              </Link>
            </div>
          ) : (
            plans.map(plan => (
              <div key={plan.id} className="plan-card">
                <div className="plan-header">
                  <div className="plan-user">
                    {plan.userProfileImage ? (
                      <img 
                        src={plan.userProfileImage.startsWith('data:') 
                          ? plan.userProfileImage 
                          : plan.userProfileImage.startsWith('http') 
                            ? plan.userProfileImage 
                            : `data:image/jpeg;base64,${plan.userProfileImage}`
                        } 
                        alt={plan.userName} 
                        className="user-avatar"
                        onError={(e) => {
                          e.target.onerror = null;
                          e.target.src = `https://ui-avatars.com/api/?name=${encodeURIComponent(plan.userName)}&background=667eea&color=fff`;
                        }}
                      />
                    ) : (
                      <div className="default-avatar">
                        <FaUser />
                      </div>
                    )}
                    <span className="user-name">{plan.userName}</span>
                  </div>
                  {user && plan.userId === user.id && (
                    <div className="plan-actions">
                      <button 
                        className="action-btn edit-btn"
                        onClick={() => startEditing(plan)}
                        title="Edit plan"
                      >
                        <FaEdit />
                      </button>
                      <button 
                        className="action-btn delete-btn"
                        onClick={() => handleDeletePlan(plan.id)}
                        title="Delete plan"
                      >
                        <FaTrash />
                      </button>
                    </div>
                  )}
                </div>

                {editingPlan === plan.id ? (
                  <div className="edit-form">
                    <input
                      type="text"
                      value={editForm.title}
                      onChange={(e) => setEditForm({ ...editForm, title: e.target.value })}
                      placeholder="Plan title"
                      className="edit-input"
                    />
                    <textarea
                      value={editForm.topics}
                      onChange={(e) => setEditForm({ ...editForm, topics: e.target.value })}
                      placeholder="Topics"
                      className="edit-textarea"
                    />
                    <textarea
                      value={editForm.resources}
                      onChange={(e) => setEditForm({ ...editForm, resources: e.target.value })}
                      placeholder="Resources"
                      className="edit-textarea"
                    />
                    <input
                      type="text"
                      value={editForm.timeline}
                      onChange={(e) => setEditForm({ ...editForm, timeline: e.target.value })}
                      placeholder="Timeline"
                      className="edit-input"
                    />
                    <div className="edit-actions">
                      <button 
                        className="btn btn-primary"
                        onClick={() => handleEditPlan(plan.id)}
                      >
                        Save
                      </button>
                      <button 
                        className="btn btn-outline"
                        onClick={cancelEditing}
                      >
                        Cancel
                      </button>
                    </div>
                  </div>
                ) : (
                  <div className="plan-content">
                    <h3 className="plan-title">
                      <FaBullseye className="plan-icon" />
                      {plan.title}
                    </h3>
                    
                    <div className="plan-details">
                      <div className="plan-section">
                        <div className="section-header">
                          <FaBook className="section-icon" />
                          <span className="section-title">Topics</span>
                        </div>
                        <p className="section-content">{plan.topics}</p>
                      </div>
                      
                      <div className="plan-section">
                        <div className="section-header">
                          <FaGraduationCap className="section-icon" />
                          <span className="section-title">Resources</span>
                        </div>
                        <p className="section-content">{plan.resources}</p>
                      </div>
                      
                      <div className="plan-section">
                        <div className="section-header">
                          <FaClock className="section-icon" />
                          <span className="section-title">Timeline</span>
                        </div>
                        <p className="section-content">{plan.timeline}</p>
                      </div>
                    </div>
                  </div>
                )}
              </div>
            ))
          )}
        </div>
      </div>
    </div>
  );
};

export default PlanList;