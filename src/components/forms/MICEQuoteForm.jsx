import React, { useState } from 'react';

const MICEQuoteForm = ({ offreId, onSubmit, isLoading }) => {
  const [formData, setFormData] = useState({
    offreId: offreId || '',
    dateSouhaitee: '',
    nbParticipants: '',
    dureeJours: '',
    equipementRequis: [],
    message: ''
  });

  const equipementOptions = [
    { value: 'videoprojecteur', label: 'Vidéoprojecteur 4K' },
    { value: 'sonorisation', label: 'Sonorisation & Micros HF' },
    { value: 'pause_cafe', label: 'Pause Café / Collation' },
    { value: 'dejeuner_traiteur', label: 'Déjeuner Traiteur (Buffet/Assis)' },
    { value: 'paperboard', label: 'Paperboard & Fournitures' }
  ];

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleCheckbox = (e) => {
    const value = e.target.value;
    setFormData((prev) => {
      const newEquipements = prev.equipementRequis.includes(value)
        ? prev.equipementRequis.filter(item => item !== value)
        : [...prev.equipementRequis, value];
      return { ...prev, equipementRequis: newEquipements };
    });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    const payload = {
      ...formData,
      offreId: parseInt(formData.offreId, 10),
      nbParticipants: parseInt(formData.nbParticipants, 10),
      dureeJours: parseInt(formData.dureeJours, 10)
    };
    onSubmit(payload);
  };

  return (
    <form onSubmit={handleSubmit} className="space-y-6">
      <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
        <div>
          <label className="block text-slate-700 font-bold mb-2 text-lg">Date de l'événement</label>
          <input type="date" name="dateSouhaitee" required value={formData.dateSouhaitee} onChange={handleChange} className="w-full px-4 py-3 rounded-xl border-2 border-slate-200 focus:border-blue-700 focus:ring-0 text-lg transition-colors outline-none" />
        </div>
        <div>
          <label className="block text-slate-700 font-bold mb-2 text-lg">Nombre de participants</label>
          <input type="number" min="1" name="nbParticipants" required value={formData.nbParticipants} onChange={handleChange} placeholder="Ex: 50" className="w-full px-4 py-3 rounded-xl border-2 border-slate-200 focus:border-blue-700 focus:ring-0 text-lg transition-colors outline-none" />
        </div>
      </div>
      
      <div>
        <label className="block text-slate-700 font-bold mb-2 text-lg">Durée (Jours)</label>
        <input type="number" min="1" name="dureeJours" required value={formData.dureeJours} onChange={handleChange} placeholder="Ex: 2" className="w-full px-4 py-3 rounded-xl border-2 border-slate-200 focus:border-blue-700 focus:ring-0 text-lg transition-colors outline-none" />
      </div>

      <div>
        <label className="block text-slate-700 font-bold mb-4 text-lg">Équipements & Services Requis</label>
        <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
          {equipementOptions.map((opt) => (
            <label key={opt.value} className="flex items-center gap-4 p-4 border-2 border-slate-100 rounded-xl cursor-pointer hover:border-blue-200 hover:bg-blue-50 transition-colors">
              <input 
                type="checkbox" 
                value={opt.value}
                checked={formData.equipementRequis.includes(opt.value)}
                onChange={handleCheckbox}
                className="w-6 h-6 text-blue-800 rounded border-slate-300 focus:ring-blue-700" 
              />
              <span className="text-slate-800 font-medium text-lg">{opt.label}</span>
            </label>
          ))}
        </div>
      </div>

      <div>
        <label className="block text-slate-700 font-bold mb-2 text-lg">Déroulement & Attentes spécifiques</label>
        <textarea name="message" value={formData.message} onChange={handleChange} rows="4" className="w-full px-4 py-3 rounded-xl border-2 border-slate-200 focus:border-blue-700 focus:ring-0 text-lg transition-colors outline-none" placeholder="Décrivez le type d'événement, la disposition de salle souhaitée..."></textarea>
      </div>

      <button type="submit" disabled={isLoading} className="w-full bg-blue-900 hover:bg-blue-800 text-white font-bold py-4 px-6 rounded-xl transition-all shadow-md hover:shadow-lg text-xl flex justify-center items-center mt-4">
        {isLoading ? <div className="animate-spin rounded-full h-8 w-8 border-t-4 border-b-4 border-white"></div> : 'Demander un devis (MICE)'}
      </button>
    </form>
  );
};

export default MICEQuoteForm;
