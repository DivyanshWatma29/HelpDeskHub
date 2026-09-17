import { Component, StrictMode } from "react";
import { createRoot } from "react-dom/client";
import App from "./App";
import "./index.css";

class ErrorBoundary extends Component {
  constructor(props) {
    super(props);
    this.state = { hasError: false, error: null };
  }

  static getDerivedStateFromError(error) {
    return { hasError: true, error };
  }

  componentDidCatch(error, errorInfo) {
    console.error("Uncaught application error:", error, errorInfo);
  }

  render() {
    if (this.state.hasError) {
      return (
        <div style={{
          minHeight: "100vh",
          display: "flex",
          alignItems: "center",
          justifyContent: "center",
          padding: "24px",
          background: "#f1f5f9",
          fontFamily: "Inter, system-ui, -apple-system, sans-serif"
        }}>
          <div style={{
            background: "#ffffff",
            borderRadius: "12px",
            padding: "32px",
            maxWidth: "480px",
            width: "100%",
            boxShadow: "0 10px 25px rgba(0,0,0,0.08)",
            textAlign: "center"
          }}>
            <h2 style={{ margin: "0 0 12px", color: "#0f172a" }}>SupportDesk</h2>
            <p style={{ color: "#64748b", margin: "0 0 20px" }}>
              An unexpected error occurred while loading the view.
            </p>
            {this.state.error?.message && (
              <pre style={{
                background: "#f8fafc",
                padding: "12px",
                borderRadius: "8px",
                color: "#dc2626",
                fontSize: "13px",
                textAlign: "left",
                overflowX: "auto",
                marginBottom: "20px"
              }}>
                {this.state.error.message}
              </pre>
            )}
            <button
              onClick={() => window.location.reload()}
              style={{
                background: "#2563eb",
                color: "#ffffff",
                border: "none",
                borderRadius: "8px",
                padding: "10px 20px",
                fontWeight: "600",
                cursor: "pointer",
                fontSize: "14px"
              }}
            >
              Reload Page
            </button>
          </div>
        </div>
      );
    }
    return this.props.children;
  }
}

createRoot(document.getElementById("root")).render(
  <StrictMode>
    <ErrorBoundary>
      <App />
    </ErrorBoundary>
  </StrictMode>
);
