import React from 'react';
import { Outlet } from 'react-router-dom';
import Navbar from '../common/Navbar';
import Footer from '../common/Footer';

const ClientDashboardLayout = () => {
  return (
    <div className="flex flex-col min-h-screen bg-slate-50">
      <Navbar />
      <div className="flex-grow flex flex-col md:flex-row w-full max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8 gap-8">
        {/* Simple Sidebar Placeholder */}
        <aside className="w-full md:w-64 bg-white rounded-xl shadow-sm border border-slate-200 p-6 flex-shrink-0 h-fit">
           <h2 className="text-xl font-bold text-blue-900 mb-6">Mon Espace</h2>
           <nav className="space-y-2">
             <a href="/client/dashboard" className="block px-4 py-2 rounded-md bg-teal-50 text-teal-700 font-medium">Mes Devis</a>
             <a href="#" className="block px-4 py-2 rounded-md text-slate-600 hover:bg-slate-50 font-medium">Mon Profil</a>
           </nav>
        </aside>
        
        {/* Main Dashboard Content */}
        <main className="flex-grow bg-white rounded-xl shadow-sm border border-slate-200 p-6 md:p-8">
          <Outlet />
        </main>
      </div>
      <Footer />
    </div>
  );
};

export default ClientDashboardLayout;
