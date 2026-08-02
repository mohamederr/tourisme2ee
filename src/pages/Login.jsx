import React, { useState, useContext } from 'react';
import { useNavigate, Link, useLocation } from 'react-router-dom';
import { AuthContext } from '../context/AuthContext';
import { authService } from '../services/authService';

const Login = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  
  const { login } = useContext(AuthContext);
  const navigate = useNavigate();
  const location = useLocation();
  const from = location.state?.from?.pathname || "/client/dashboard";

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setLoading(true);
    
    try {
      // Simulate API call for now (or use actual if backend is ready)
      // const data = await authService.login({ email, password });
      // login(data.user, data.token);
      
      setTimeout(() => {
         login({ id: 1, email, role: 'CLIENT', nom: 'Association Horizon' }, 'fake-jwt-token-123');
         navigate(from, { replace: true });
      }, 1000);
      
    } catch (err) {
      setError('Identifiants incorrects');
      setLoading(false);
    }
  };

  return (
    <div className="w-full max-w-sm mx-auto">
      <div className="text-center mb-8">
        <h2 className="text-3xl font-bold text-blue-900 mb-3">Connexion</h2>
        <p className="text-slate-600 font-medium">Accédez à votre espace B2B/B2G</p>
      </div>
      
      {error && <div className="bg-red-50 text-red-600 p-4 rounded-xl mb-6 text-center font-bold border border-red-100">{error}</div>}

      <form onSubmit={handleSubmit} className="space-y-6">
        <div>
          <label className="block text-slate-700 font-bold mb-2">Email Pro / Association</label>
          <input type="email" required value={email} onChange={e => setEmail(e.target.value)} className="w-full px-4 py-3 rounded-xl border-2 border-slate-200 focus:ring-0 focus:border-teal-500 outline-none text-lg transition-colors" placeholder="contact@association.fr" />
        </div>
        <div>
          <label className="block text-slate-700 font-bold mb-2">Mot de passe</label>
          <input type="password" required value={password} onChange={e => setPassword(e.target.value)} className="w-full px-4 py-3 rounded-xl border-2 border-slate-200 focus:ring-0 focus:border-teal-500 outline-none text-lg transition-colors" placeholder="••••••••" />
        </div>
        
        <button type="submit" disabled={loading} className="w-full bg-teal-600 hover:bg-teal-500 text-white font-bold py-4 px-4 rounded-xl transition-all shadow-md hover:shadow-lg text-lg flex justify-center items-center mt-2">
          {loading ? <div className="animate-spin rounded-full h-6 w-6 border-t-2 border-b-2 border-white"></div> : 'Se connecter'}
        </button>
      </form>
      
      <div className="mt-8 text-center text-slate-600 font-medium">
        Pas encore de compte ? <Link to="/register" className="text-teal-600 font-bold hover:underline ml-1">S'inscrire</Link>
      </div>
    </div>
  );
};

export default Login;
