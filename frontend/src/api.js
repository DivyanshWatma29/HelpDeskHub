const API_BASE = import.meta.env.VITE_API_BASE ?? "/api";

function authorizationHeader(credentials) {
  if (!credentials) return {};
  return { Authorization: `Basic ${btoa(`${credentials.username}:${credentials.password}`)}` };
}

async function request(path, options = {}, credentials) {
  const response = await fetch(`${API_BASE}${path}`, {
    ...options,
    headers: {
      ...(options.body ? { "Content-Type": "application/json" } : {}),
      ...authorizationHeader(credentials),
      ...options.headers
    }
  });

  const isJson = response.headers.get("content-type")?.includes("application/json");
  const data = isJson ? await response.json() : null;

  if (!response.ok) {
    if (response.status === 401) {
      throw new Error("Invalid username or password.");
    }
    const message = data
      ? (typeof data === "object" ? Object.values(data).join(" ") : data)
      : "The request could not be completed.";
    throw new Error(message);
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
