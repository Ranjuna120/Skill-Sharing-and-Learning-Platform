import React, { useState } from 'react';
import './UtilsDemo.css';

const UtilsDemo = () => {
    const [stringText, setStringText] = useState('');
    const [stringResult, setStringResult] = useState(null);
    const [validationResult, setValidationResult] = useState(null);
    const [loading, setLoading] = useState(false);

    // String Utils Demo
    const handleStringDemo = async () => {
        if (!stringText.trim()) return;
        
        setLoading(true);
        try {
            const response = await fetch(`http://localhost:8081/api/demo/string-utils?text=${encodeURIComponent(stringText)}`);
            const data = await response.json();
            setStringResult(data);
        } catch (error) {
            console.error('Error:', error);
        }
        setLoading(false);
    };

    // Validation Demo
    const handleValidationDemo = async () => {
        setLoading(true);
        try {
            const response = await fetch('http://localhost:8081/api/demo/validation-utils');
            const data = await response.json();
            setValidationResult(data);
        } catch (error) {
            console.error('Error:', error);
        }
        setLoading(false);
    };

    // Math Utils Demo
    const [mathNumbers, setMathNumbers] = useState('1,2,3,4,5');
    const [mathResult, setMathResult] = useState(null);

    const handleMathDemo = async () => {
        const numbers = mathNumbers.split(',').map(n => parseFloat(n.trim())).filter(n => !isNaN(n));
        if (numbers.length === 0) return;

        setLoading(true);
        try {
            const response = await fetch('http://localhost:8081/api/demo/math-utils');
            const data = await response.json();
            setMathResult(data);
        } catch (error) {
            console.error('Error:', error);
        }
        setLoading(false);
    };

    // Security Utils Demo
    const [securityResult, setSecurityResult] = useState(null);

    const handleSecurityDemo = async () => {
        setLoading(true);
        try {
            const response = await fetch('http://localhost:8081/api/demo/security-utils');
            const data = await response.json();
            setSecurityResult(data);
        } catch (error) {
            console.error('Error:', error);
        }
        setLoading(false);
    };

    // Date Utils Demo
    const [dateResult, setDateResult] = useState(null);

    const handleDateDemo = async () => {
        setLoading(true);
        try {
            const response = await fetch('http://localhost:8081/api/demo/date-utils');
            const data = await response.json();
            setDateResult(data);
        } catch (error) {
            console.error('Error:', error);
        }
        setLoading(false);
    };

    // Collection Utils Demo
    const [collectionResult, setCollectionResult] = useState(null);

    const handleCollectionDemo = async () => {
        setLoading(true);
        try {
            const response = await fetch('http://localhost:8081/api/demo/collection-utils');
            const data = await response.json();
            setCollectionResult(data);
        } catch (error) {
            console.error('Error:', error);
        }
        setLoading(false);
    };

    // All Utils Demo
    const [allUtilsResult, setAllUtilsResult] = useState(null);

    const handleAllUtilsDemo = async () => {
        setLoading(true);
        try {
            const response = await fetch('http://localhost:8081/api/demo/all-utils');
            const data = await response.json();
            setAllUtilsResult(data);
        } catch (error) {
            console.error('Error:', error);
        }
        setLoading(false);
    };

    return (
        <div className="utils-demo-container">
        <div className="utils-demo">
            <div className="utils-header">
                <h1>🛠️ Java Utility Classes Demo</h1>
                <p>Explore the powerful utility classes added to the backend</p>
            </div>

            {/* String Utils Section */}
            <div className="demo-section">
                <h2>📝 String Utils</h2>
                <div className="demo-controls">
                    <input
                        type="text"
                        placeholder="Enter text to process..."
                        value={stringText}
                        onChange={(e) => setStringText(e.target.value)}
                        className="demo-input"
                    />
                    <button onClick={handleStringDemo} disabled={loading || !stringText.trim()}>
                        Process String
                    </button>
                </div>
                {stringResult && (
                    <div className="demo-result">
                        <h3>Results:</h3>
                        <div className="result-grid">
                            <div className="result-item">
                                <strong>Original:</strong> "{stringResult.original}"
                            </div>
                            <div className="result-item">
                                <strong>Is Empty:</strong> {stringResult.isEmpty ? 'Yes' : 'No'}
                            </div>
                            <div className="result-item">
                                <strong>Is Blank:</strong> {stringResult.isBlank ? 'Yes' : 'No'}
                            </div>
                            <div className="result-item">
                                <strong>Normalized:</strong> "{stringResult.normalized}"
                            </div>
                            <div className="result-item">
                                <strong>Capitalized:</strong> "{stringResult.capitalized}"
                            </div>
                            <div className="result-item">
                                <strong>Camel Case:</strong> "{stringResult.camelCase}"
                            </div>
                            <div className="result-item">
                                <strong>Snake Case:</strong> "{stringResult.snakeCase}"
                            </div>
                            <div className="result-item">
                                <strong>Kebab Case:</strong> "{stringResult.kebabCase}"
                            </div>
                            <div className="result-item">
                                <strong>Reversed:</strong> "{stringResult.reversed}"
                            </div>
                            <div className="result-item">
                                <strong>Is Numeric:</strong> {stringResult.isNumeric ? 'Yes' : 'No'}
                            </div>
                            <div className="result-item">
                                <strong>Is Alphabetic:</strong> {stringResult.isAlphabetic ? 'Yes' : 'No'}
                            </div>
                            <div className="result-item">
                                <strong>Is Alphanumeric:</strong> {stringResult.isAlphanumeric ? 'Yes' : 'No'}
                            </div>
                            {stringResult.truncated && (
                                <div className="result-item">
                                    <strong>Truncated:</strong> "{stringResult.truncated}"
                                </div>
                            )}
                            {stringResult.truncatedWithEllipsis && (
                                <div className="result-item">
                                    <strong>Truncated with Ellipsis:</strong> "{stringResult.truncatedWithEllipsis}"
                                </div>
                            )}
                        </div>
                    </div>
                )}
            </div>
            

            {/* Validation Utils Section */}
            <div className="demo-section">
                <h2>✅ Validation Utils</h2>
                <div className="demo-controls">
                    <button onClick={handleValidationDemo} disabled={loading}>
                        Test Validation Examples
                    </button>
                </div>
                {validationResult && (
                    <div className="demo-result">
                        <h3>Validation Test Results:</h3>
                        <div className="validation-results">
                            {Object.entries(validationResult).map(([category, tests]) => (
                                <div key={category} className="validation-category">
                                    <h4>{category.replace(/([A-Z])/g, ' $1').toLowerCase()}</h4>
                                    <div className="test-results">
                                        {Object.entries(tests).map(([input, isValid]) => (
                                            <div key={input} className="test-item">
                                                <span className="test-input">"{input}"</span>
                                                <span className={isValid ? 'valid' : 'invalid'}>
                                                    {isValid ? '✅ Valid' : '❌ Invalid'}
                                                </span>
                                            </div>
                                        ))}
                                    </div>
                                </div>
                            ))}
                        </div>
                    </div>
                )}
            </div>

            {/* Math Utils Section */}
            <div className="demo-section">
                <h2>🔢 Math Utils</h2>
                <div className="demo-controls">
                    <input
                        type="text"
                        placeholder="Enter numbers (comma separated): 1,2,3,4,5"
                        value={mathNumbers}
                        onChange={(e) => setMathNumbers(e.target.value)}
                        className="demo-input wide-input"
                    />
                    <button onClick={handleMathDemo} disabled={loading}>
                        Calculate Statistics
                    </button>
                </div>
                {mathResult && (
                    <div className="demo-result">
                        <h3>Mathematical Results:</h3>
                        <div className="result-grid">
                            {Object.entries(mathResult).map(([key, value]) => (
                                <div key={key} className="result-item">
                                    <strong>{key.replace(/([A-Z])/g, ' $1').toLowerCase()}:</strong> {value}
                                </div>
                            ))}
                        </div>
                    </div>
                )}
            </div>

            {/* Security Utils Section */}
            <div className="demo-section">
                <h2>🔐 Security Utils</h2>
                <div className="demo-controls">
                    <button onClick={handleSecurityDemo} disabled={loading}>
                        Generate Security Tokens
                    </button>
                </div>
                {securityResult && (
                    <div className="demo-result">
                        <h3>Security Generation Results:</h3>
                        <div className="security-results">
                            {Object.entries(securityResult).map(([key, value]) => (
                                <div key={key} className="result-item">
                                    <strong>{key}:</strong>
                                    <code className="security-value">{value}</code>
                                </div>
                            ))}
                        </div>
                    </div>
                )}
            </div>

            {/* Date Utils Section */}
            <div className="demo-section">
                <h2>📅 Date Utils</h2>
                <div className="demo-controls">
                    <button onClick={handleDateDemo} disabled={loading}>
                        Generate Date Examples
                    </button>
                </div>
                {dateResult && (
                    <div className="demo-result">
                        <h3>Date Utilities Results:</h3>
                        <div className="result-grid">
                            {Object.entries(dateResult).map(([key, value]) => (
                                <div key={key} className="result-item">
                                    <strong>{key.replace(/([A-Z_])/g, ' $1').toLowerCase()}:</strong> 
                                    {typeof value === 'object' ? JSON.stringify(value) : value}
                                </div>
                            ))}
                        </div>
                    </div>
                )}
            </div>

            {/* Collection Utils Section */}
            <div className="demo-section">
                <h2>📋 Collection Utils</h2>
                <div className="demo-controls">
                    <button onClick={handleCollectionDemo} disabled={loading}>
                        Demonstrate Collections
                    </button>
                </div>
                {collectionResult && (
                    <div className="demo-result">
                        <h3>Collection Utilities Results:</h3>
                        <div className="result-grid">
                            {Object.entries(collectionResult).map(([key, value]) => (
                                <div key={key} className="result-item">
                                    <strong>{key.replace(/([A-Z_])/g, ' $1').toLowerCase()}:</strong> 
                                    {typeof value === 'object' ? JSON.stringify(value) : value}
                                </div>
                            ))}
                        </div>
                    </div>
                )}
            </div>

            {/* Test All Utilities Section */}
            <div className="demo-section all-utils-section">
                <h2>🚀 Test All Utilities</h2>
                <div className="demo-controls">
                    <p style={{margin: 0, color: '#6c757d', fontSize: '1.1rem'}}>
                        Run all utility demonstrations at once to see the complete showcase!
                    </p>
                    <button onClick={handleAllUtilsDemo} disabled={loading} className="primary-button">
                        🎯 Test All Utilities
                    </button>
                </div>
                {allUtilsResult && (
                    <div className="demo-result">
                        <h3>Complete Utilities Showcase:</h3>
                        <div className="all-utils-grid">
                            {Object.entries(allUtilsResult).map(([utilName, utilData]) => {
                                if (utilName === 'summary') {
                                    return (
                                        <div key={utilName} className="summary-card">
                                            <h4>📊 Summary</h4>
                                            <div className="summary-content">
                                                {Object.entries(utilData).map(([key, value]) => (
                                                    <div key={key} className="summary-item">
                                                        <strong>{key.replace(/([A-Z])/g, ' $1').toLowerCase()}:</strong> {value}
                                                    </div>
                                                ))}
                                            </div>
                                        </div>
                                    );
                                }
                                return (
                                    <div key={utilName} className="util-showcase-card">
                                        <h4>{utilName.replace(/([A-Z])/g, ' $1').toLowerCase()}</h4>
                                        <div className="util-data">
                                            {utilData && typeof utilData === 'object' ? (
                                                Object.entries(utilData).slice(0, 3).map(([key, value]) => (
                                                    <div key={key} className="util-item">
                                                        <span className="util-key">{key}:</span>
                                                        <span className="util-value">
                                                            {typeof value === 'object' ? 
                                                                JSON.stringify(value).substring(0, 50) + '...' : 
                                                                String(value).substring(0, 50) + (String(value).length > 50 ? '...' : '')
                                                            }
                                                        </span>
                                                    </div>
                                                ))
                                            ) : (
                                                <div className="util-item">No data</div>
                                            )}
                                        </div>
                                    </div>
                                );
                            })}
                        </div>
                    </div>
                )}
            </div>

            {loading && (
                <div className="loading-overlay">
                    <div className="loading-spinner">🔄 Processing...</div>
                </div>
            )}
        </div>
        </div>
    );
};

export default UtilsDemo;
