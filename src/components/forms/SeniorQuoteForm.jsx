import React, { useState } from 'react';

const SeniorQuoteForm = ({ offreId, onSubmit, isLoading }) => {
  const [formData, setFormData] = useState({
    offreId: offreId || '',
    dateSouhaitee: '',
    nbParticipants: '',
    paysOrigine: 'France',
    besoinsSpecifiques: '',
    message: ''
  });

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    const payload = {
      ...formData,
      offreId: parseInt(formData.offreId, 10),
      nbParticipants: parseInt(formData.nbParticipants, 10)
    };
    onSubmit(payload);
  };

  return (
    <form onSubmit={handleSubmit} className="space-y-6">
      <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
        <div>
          <label className="block text-slate-700 font-bold mb-2 text-lg">Date souhaitée</label>
          <input type="date" name="dateSouhaitee" required value={formData.dateSouhaitee} onChange={handleChange} className="w-full px-4 py-3 rounded-xl border-2 border-slate-200 focus:border-teal-500 focus:ring-0 text-lg transition-colors outline-none" />
        </div>
        <div>
          <label className="block text-slate-700 font-bold mb-2 text-lg">Nombre de participants</label>
          <input type="number" min="1" name="nbParticipants" required value={formData.nbParticipants} onChange={handleChange} placeholder="Ex: 18" className="w-full px-4 py-3 rounded-xl border-2 border-slate-200 focus:border-teal-500 focus:ring-0 text-lg transition-colors outline-none" />
        </div>
      </div>
      
      <div>
        <label className="block text-slate-700 font-bold mb-2 text-lg">Pays d'origine</label>
        <select name="paysOrigine" value={formData.paysOrigine} onChange={handleChange} className="w-full px-4 py-3 rounded-xl border-2 border-slate-200 focus:border-teal-500 focus:ring-0 text-lg transition-colors outline-none">
          <option value="Maroc">Maroc</option>
          <option value="France">France</option>
          <option value="Espagne">Espagne</option>
          <option value="Belgique">Belgique</option>
          <option value="Autre">Autre</option>
        </select>
      </div>

      <div>
        <label className="block text-slate-700 font-bold mb-2 text-lg">Besoins spécifiques (PMR, Régimes...)</label>
        <textarea name="besoinsSpecifiques" value={formData.besoinsSpecifiques} onChange={handleChange} rows="3" placeholder="Accessibilité fauteuils roulants, régimes alimentaires spécifiques..." className="w-full px-4 py-3 rounded-xl border-2 border-slate-200 focus:border-teal-500 focus:ring-0 text-lg transition-colors outline-none"></textarea>
      </div>

      <div>
        <label className="block text-slate-700 font-bold mb-2 text-lg">Message / Autre demande</label>
        <textarea name="message" value={formData.message} onChange={handleChange} rows="3" className="w-full px-4 py-3 rounded-xl border-2 border-slate-200 focus:border-teal-500 focus:ring-0 text-lg transition-colors outline-none"></textarea>
      </div>

      <button type="submit" disabled={isLoading} className="w-full bg-teal-600 hover:bg-teal-500 text-white font-bold py-4 px-6 rounded-xl transition-all shadow-md hover:shadow-lg text-xl flex justify-center items-center mt-4">
        {isLoading ? <div className="animate-spin rounded-full h-8 w-8 border-t-4 border-b-4 border-white"></div> : 'Envoyer la demande (Senior)'}
      </button>
    </form>
  );
};

export default SeniorQuoteForm;
