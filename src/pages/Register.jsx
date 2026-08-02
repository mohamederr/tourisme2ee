import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { authService } from '../services/authService';

const Register = () => {
  const [formData, setFormData] = useState({
    nom: '',
    prenom: '',
    email: '',
    motDePasse: '',
    organisation: '',
    pays: 'Maroc',
    typeProfil: 'ASSOCIATION'
  });
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  const handleChange = (e) => setFormData({...formData, [e.target.name]: e.target.value});

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setLoading(true);
    try {
      // await authService.register(formData);
      setTimeout(() => {
        navigate('/login', { state: { message: 'Inscription réussie. Vous pouvez vous connecter.' } });
      }, 1000);
    } catch (err) {
      setError('Erreur lors de l\'inscription');
      setLoading(false);
    }
  };

  return (
    <div className="w-full max-w-md mx-auto">
      <div className="text-center mb-8">
        <h2 className="text-3xl font-bold text-blue-900 mb-3">Créer un compte</h2>
        <p className="text-slate-600 font-medium">Rejoignez-nous pour gérer vos séjours de groupes.</p>
      </div>
      
      {error && <div className="bg-red-50 text-red-600 p-4 rounded-xl mb-6 text-center font-bold border border-red-100">{error}</div>}

      <form onSubmit={handleSubmit} className="space-y-5">
        <div className="grid grid-cols-2 gap-5">
          <div>
            <label className="block text-slate-700 font-bold mb-2">Nom</label>
            <input type="text" name="nom" required onChange={handleChange} className="w-full px-4 py-3 rounded-xl border-2 border-slate-200 focus:border-teal-500 focus:ring-0 transition-colors outline-none" />
          </div>
          <div>
            <label className="block text-slate-700 font-bold mb-2">Prénom</label>
            <input type="text" name="prenom" required onChange={handleChange} className="w-full px-4 py-3 rounded-xl border-2 border-slate-200 focus:border-teal-500 focus:ring-0 transition-colors outline-none" />
          </div>
        </div>
        
        <div>
          <label className="block text-slate-700 font-bold mb-2">Type de Profil</label>
          <select name="typeProfil" onChange={handleChange} className="w-full px-4 py-3 rounded-xl border-2 border-slate-200 focus:border-teal-500 focus:ring-0 transition-colors outline-none font-medium">
            <option value="ASSOCIATION">Association (Seniors / Retraités)</option>
            <option value="ENTREPRISE">Entreprise / Agence (MICE)</option>
          </select>
        </div>

        <div>
          <label className="block text-slate-700 font-bold mb-2">Nom de l'Organisation</label>
          <input type="text" name="organisation" required onChange={handleChange} className="w-full px-4 py-3 rounded-xl border-2 border-slate-200 focus:border-teal-500 focus:ring-0 transition-colors outline-none" />
        </div>

        <div>
          <label className="block text-slate-700 font-bold mb-2">Email Pro / Association</label>
          <input type="email" name="email" required onChange={handleChange} className="w-full px-4 py-3 rounded-xl border-2 border-slate-200 focus:border-teal-500 focus:ring-0 transition-colors outline-none" />
        </div>

        <div>
          <label className="block text-slate-700 font-bold mb-2">Mot de passe</label>
          <input type="password" name="motDePasse" required minLength="6" onChange={handleChange} className="w-full px-4 py-3 rounded-xl border-2 border-slate-200 focus:border-teal-500 focus:ring-0 transition-colors outline-none" />
        </div>

        <button type="submit" disabled={loading} className="w-full bg-teal-600 hover:bg-teal-500 text-white font-bold py-4 px-4 rounded-xl transition-all shadow-md hover:shadow-lg text-lg flex justify-center items-center mt-4">
          {loading ? <div className="animate-spin rounded-full h-6 w-6 border-t-2 border-b-2 border-white"></div> : 'Créer mon compte'}
        </button>
      </form>
      
      <div className="mt-8 text-center text-slate-600 font-medium">
        Déjà un compte ? <Link to="/login" className="text-teal-600 font-bold hover:underline ml-1">Se connecter</Link>
      </div>
    </div>
  );
};

export default Register;
