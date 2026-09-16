const DEFAULT_PROD_API = "https://helpdeskhub-api.onrender.com/api";

const isLocalhost =
  typeof window !== "undefined" &&
  (window.location.hostname === "localhost" ||
    window.location.hostname === "127.0.0.1" ||
    window.location.hostname === "0.0.0.0");

const API_BASE =
  import.meta.env.VITE_API_BASE || (isLocalhost ? "/api" : DEFAULT_PROD_API);

function authorizationHeader(credentials) {
  if (!credentials) return {};
  return { Authorization: `Basic ${btoa(`${credentials.username}:${credentials.password}`)}` };
}

async function request(path, options = {}, credentials) {
  let response;
  try {
    response = await fetch(`${API_BASE}${path}`, {
      ...options,
      headers: {
        ...(options.body ? { "Content-Type": "application/json" } : {}),
        ...authorizationHeader(credentials),
        ...options.headers
      }
    });
  } catch {
    throw new Error(
      "Cannot connect to the server. If this is a free-tier cloud deployment, the API may take 30-50 seconds to wake up."
    );
  }

  const contentType = response.headers.get("content-type") || "";
  const isJson = contentType.includes("application/json");

  let data = null;
  if (isJson) {
    try {
      data = await response.json();
    } catch {
      data = null;
    }
  }

  if (!response.ok) {
    if (response.status === 401) {
      throw new Error("Invalid username or password.");
    }
    const message = data
      ? (typeof data === "object" ? Object.values(data).join(" ") : data)
      : `Request failed with status ${response.status}`;
    throw new Error(message);
  }

  if (!isJson) {
    throw new Error("Backend service returned a non-JSON response.");
  }

  return data;
}

export function checkAuth(credentials) {
  return request("/auth/me", {}, credentials);
}

export function getDepartments() {
  return request("/departments");
}

export function createTicket(ticket) {
  return request("/tickets", { method: "POST", body: JSON.stringify(ticket) });
}

export function getTickets(credentials, filters = {}) {
  const params = new URLSearchParams();
  if (filters.departmentId) params.set("departmentId", filters.departmentId);
  if (filters.status) params.set("status", filters.status);
  if (filters.priority) params.set("priority", filters.priority);
  const query = params.toString() ? `?${params}` : "";
  return request(`/tickets${query}`, {}, credentials);
}

export function getTicketDetails(id, credentials) {
  return request(`/tickets/${id}`, {}, credentials);
}

export function trackTicket(ticketNumber) {
  return request(`/tickets/track/${encodeURIComponent(ticketNumber)}`);
}

export function getDashboardSummary(credentials) {
  return request("/dashboard/summary", {}, credentials);
}

export function updateTicket(id, update, credentials) {
  return request(`/tickets/${id}`, { method: "PUT", body: JSON.stringify(update) }, credentials);
}

export function deleteTicket(id, credentials) {
  return request(`/tickets/${id}`, { method: "DELETE" }, credentials);
}
