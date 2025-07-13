import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { FaPlus, FaUser, FaEdit, FaTrash } from 'react-icons/fa';
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
    return <div className="plan-list-container">Loading plans...</div>;
  }

  return (
    <div className="plan-list-container">
      <div className="plan-list-header">
        <h1 className="plan-list-title">Learning Plans</h1>
        <Link to="/create-plan" className="create-button">
          <FaPlus className="create-icon" />
          <span>Create Plan</span>
        </Link>
      </div>
      {error && <p className="plan-list-error">{error}</p>}
      <div className="plan-list">
        {plans.length === 0 ? (
          <p className="no-plans">No plans available. Create one now!</p>
        ) : (
          plans.map(plan => (
            <div key={plan.id} className="plan-card">
              <div className="plan-user-info">
                <div className="user-profile">
                  {plan.userProfileImage ? (
                    <img 
                      src={plan.userProfileImage} 
                      alt={plan.userName} 
                      className="profile-image"
                    />
                  ) : (
                    <FaUser className="default-profile-icon" />
                  )}
                  <span className="user-name">{plan.userName}</span>
                </div>
                {user && plan.userId === user.id && (
                  <div className="plan-actions-owner">
                    <button 
                      className="edit-btn"
                      onClick={() => startEditing(plan)}
                    >
                      <FaEdit />
                    </button>
                    <button 
                      className="delete-btn"
                      onClick={() => handleDeletePlan(plan.id)}
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
                    placeholder="Title"
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
                      className="save-btn"
                      onClick={() => handleEditPlan(plan.id)}
                    >
                      Save
                    </button>
                    <button 
                      className="cancel-btn"
                      onClick={cancelEditing}
                    >
                      Cancel
                    </button>
                  </div>
                </div>
              ) : (
                <>
                  <h2 className="plan-title">{plan.title}</h2>
                  <div className="plan-details">
                    <p className="plan-detail">
                      <strong>Topics:</strong> {plan.topics}
                    </p>
                    <p className="plan-detail">
                      <strong>Resources:</strong> {plan.resources}
                    </p>
                    <p className="plan-detail">
                      <strong>Timeline:</strong> {plan.timeline}
                    </p>
                  </div>
                </>
              )}
            </div>
          ))
        )}
      </div>
    </div>
  );
};

export default PlanList;