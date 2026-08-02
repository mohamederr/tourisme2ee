import React from 'react';
import { Routes, Route } from 'react-router-dom';
import MainLayout from '../components/layout/MainLayout';
import AuthLayout from '../components/layout/AuthLayout';
import ClientDashboardLayout from '../components/layout/ClientDashboardLayout';
import ProtectedRoute from './ProtectedRoute';

// Pages
import Home from '../pages/Home';
import OffresCatalog from '../pages/OffresCatalog';
import OffreDetail from '../pages/OffreDetail';
import QuoteRequest from '../pages/QuoteRequest';
import Login from '../pages/Login';
import Register from '../pages/Register';
import ClientDashboard from '../pages/client/ClientDashboard';

const AppRoutes = () => {
  return (
    <Routes>
      {/* Public Routes with MainLayout (Navbar + Footer) */}
      <Route element={<MainLayout />}>
        <Route path="/" element={<Home />} />
        <Route path="/catalog" element={<OffresCatalog />} />
        <Route path="/offer/:id" element={<OffreDetail />} />
        <Route path="/quote/:id" element={<QuoteRequest />} />
        <Route path="/quote" element={<QuoteRequest />} />
      </Route>

      {/* Auth Routes */}
      <Route element={<AuthLayout />}>
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />
      </Route>

      {/* Protected Client Routes */}
      <Route 
        path="/client" 
        element={
          <ProtectedRoute allowedRoles={['CLIENT']}>
            <ClientDashboardLayout />
          </ProtectedRoute>
        }
      >
        <Route path="dashboard" element={<ClientDashboard />} />
      </Route>
    </Routes>
  );
};

export default AppRoutes;
