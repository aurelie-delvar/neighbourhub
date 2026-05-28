import { BrowserRouter, Navigate, Route, Routes } from 'react-router-dom';
import './App.css';
import { AuthProvider, useAuth } from './context/AuthContext';
import LoginPage from './pages/LoginPage';
import RegisterPage from './pages/RegisterPage';
import HomePage from './pages/HomePage';
import type { ReactNode } from 'react';
import AdDetailPage from './pages/AdDetailPage';
import AdFormPage from './pages/AdFormPage';
import EventsPage from './pages/EventsPage';

function PrivateRoute({ children }: { children: ReactNode }) {
  const { isAuthenticated } = useAuth();
  return isAuthenticated ? children : <Navigate to="/login"/>;
}

function App() {
  return (
    <AuthProvider>
      <BrowserRouter>
        <Routes>
          <Route path="/login" element={<LoginPage />} />
          <Route path="/register" element={<RegisterPage />} />
          <Route path="/" element={
            <PrivateRoute>
              <HomePage />
            </PrivateRoute>
          } />
          <Route path="/ad/form" element={
            <PrivateRoute>
              <AdFormPage />
            </PrivateRoute>
          } />
          <Route path="/ad/form/:id" element={
            <PrivateRoute>
              <AdFormPage />
            </PrivateRoute>
          } />
          <Route path="/ads/:id" element={
            <PrivateRoute>
              <AdDetailPage />
            </PrivateRoute>
          } />
          <Route path="/events" element={
            <PrivateRoute>
              <EventsPage />
            </PrivateRoute>
          } />
        </Routes>
      </BrowserRouter>
    </AuthProvider>
  )
}

export default App;
