import { useState, useEffect } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import "./home.css";

function Home() {
    const [user, setUser] = useState(null);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState("");
    const navigate = useNavigate();

    useEffect(() => {
        // Get username from localStorage or session
        const username = localStorage.getItem("currentUser");
        if (username) {
            setUser(username);
        }
    }, []);

    const handleLogout = async () => {
        setLoading(true);
        setError("");
        try {
            // Call logout endpoint
            await axios.post("http://localhost:3000/logout");
            
            // Clear localStorage
            localStorage.removeItem("currentUser");
            
            // Navigate to signin
            navigate("/signin");
        } catch (error) {
            setError("Logout failed. Please try again.");
            console.error("Logout error:", error);
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="home-container">
            <div className="home-header">
                <h1>Welcome to FoodHub! 🍽️</h1>
                <p className="subtitle">Your favorite food delivery platform</p>
            </div>

            <div className="home-content">
                <div className="welcome-card">
                    <div className="card-header">
                        <h2>Hello, <span className="username">{user || "User"}</span>! 👋</h2>
                        <p className="status">You are successfully logged in</p>
                    </div>

                    <div className="card-body">
                        <div className="info-section">
                            <h3>What's next?</h3>
                            <ul className="feature-list">
                                <li>✓ Browse delicious food options</li>
                                <li>✓ Add items to your cart</li>
                                <li>✓ Place your order</li>
                                <li>✓ Track your delivery</li>
                            </ul>
                        </div>

                        <div className="quick-actions">
                            <h3>Quick Actions</h3>
                            <button className="action-btn">🍕 Order Now</button>
                            <button className="action-btn">❤️ My Favorites</button>
                            <button className="action-btn">📦 Order History</button>
                        </div>
                    </div>

                    <div className="card-footer">
                        {error && <div className="alert alert-error">{error}</div>}
                        <button 
                            onClick={handleLogout}
                            className="btn-logout"
                            disabled={loading}
                        >
                            {loading ? "Logging out..." : "🚪 Logout"}
                        </button>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default Home;
