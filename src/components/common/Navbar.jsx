import React, { useContext } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { AuthContext } from '../../context/AuthContext';
import { LogOut, User, Menu } from 'lucide-react';

const Navbar = () => {
  const { user, logout } = useContext(AuthContext);
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate('/');
  };

  return (
    <nav className="bg-blue-900 text-white shadow-md sticky top-0 z-50">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex justify-between items-center h-20">
          
          {/* Logo / Brand */}
          <div className="flex-shrink-0 flex items-center">
            <Link to="/" className="flex items-center gap-3">
              <div className="w-12 h-12 bg-teal-600 rounded-full flex items-center justify-center shadow-sm">
                <span className="font-bold text-2xl text-white">2E</span>
              </div>
              <div className="flex flex-col">
                <span className="font-bold text-xl tracking-wider leading-tight text-white">TOURISME 2E</span>
                <span className="text-xs text-teal-300 font-medium tracking-wide">ONCF Estivage Saïdia</span>
              </div>
            </Link>
          </div>

          {/* Desktop Navigation Links */}
          <div className="hidden md:flex space-x-8 items-center">
            <Link to="/" className="text-slate-200 hover:text-teal-400 font-medium transition-colors text-[17px]">
              Accueil
            </Link>
            <Link to="/catalog" className="text-slate-200 hover:text-teal-400 font-medium transition-colors text-[17px]">
              Nos Offres (Senior & MICE)
            </Link>
            
            {user ? (
              <div className="flex items-center gap-4 ml-4">
                <Link 
                  to={user.role === 'ADMIN' ? "/admin/dashboard" : "/client/dashboard"} 
                  className="flex items-center gap-2 text-teal-300 hover:text-teal-100 font-medium transition-colors text-[17px]"
                >
                  <User size={22} />
                  <span>Mon Espace</span>
                </Link>
                <button 
                  onClick={handleLogout}
                  className="flex items-center gap-2 bg-slate-700 hover:bg-slate-600 px-4 py-2 rounded-md transition-colors text-[17px] font-medium ml-2"
                >
                  <LogOut size={20} />
                  <span>Déconnexion</span>
                </button>
              </div>
            ) : (
              <div className="flex items-center gap-4 ml-4">
                <Link to="/login" className="text-slate-200 hover:text-white font-medium text-[17px]">
                  Connexion
                </Link>
                <Link to="/register" className="bg-teal-600 hover:bg-teal-500 text-white px-5 py-2 rounded-md font-medium transition-colors shadow-sm text-[17px]">
                  S'inscrire
                </Link>
              </div>
            )}
          </div>

          {/* Mobile menu button */}
          <div className="md:hidden flex items-center">
            <button className="text-slate-200 hover:text-white p-2">
              <Menu size={32} />
            </button>
          </div>
        </div>
      </div>
    </nav>
  );
};

export default Navbar;
