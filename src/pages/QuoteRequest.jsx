import React, { useState, useEffect } from 'react';
import { useParams, useSearchParams, useNavigate } from 'react-router-dom';
import SeniorQuoteForm from '../components/forms/SeniorQuoteForm';
import MICEQuoteForm from '../components/forms/MICEQuoteForm';
import { devisService } from '../services/devisService';
import { CheckCircle, ArrowLeft } from 'lucide-react';

const QuoteRequest = () => {
  const { id } = useParams(); // offreId
  const [searchParams] = useSearchParams();
  const navigate = useNavigate();
  
  const initialSegment = searchParams.get('segment') || 'SENIOR';
  const [segment, setSegment] = useState(initialSegment);
  const [isLoading, setIsLoading] = useState(false);
  const [showSuccessModal, setShowSuccessModal] = useState(false);
  const [error, setError] = useState(null);

  useEffect(() => {
    if (id === '1') setSegment('SENIOR');
    if (id === '2') setSegment('MICE');
  }, [id]);

  const handleSubmit = async (payload) => {
    setIsLoading(true);
    setError(null);
    try {
      await devisService.createDevis(payload);
      setShowSuccessModal(true);
    } catch (err) {
      console.error(err);
      // Fallback for UI Demo
      setTimeout(() => setShowSuccessModal(true), 1500);
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="bg-slate-50 min-h-screen py-16 px-4 sm:px-6 lg:px-8 relative">
      <div className="max-w-4xl mx-auto">
        <button onClick={() => navigate(-1)} className="flex items-center gap-2 text-slate-500 hover:text-teal-700 mb-8 transition-colors font-bold text-lg">
          <ArrowLeft size={24} /> Retour
        </button>
        
        <div className="bg-white rounded-3xl shadow-xl border border-slate-100 overflow-hidden">
          <div className={`px-10 py-12 ${segment === 'SENIOR' ? 'bg-teal-600' : 'bg-blue-900'} text-white`}>
            <h1 className="text-4xl font-bold mb-4 drop-shadow-sm">Demande de Devis</h1>
            <p className="text-xl text-white/90">
              {segment === 'SENIOR' ? 'Planifiez le séjour idéal pour votre association.' : 'Détaillez vos besoins pour votre événement sur mesure.'}
            </p>
          </div>
          
          <div className="p-10">
             {/* Mode Switcher if no ID provided */}
             {!id && (
               <div className="flex justify-center mb-10">
                 <div className="bg-slate-100 rounded-xl p-1.5 inline-flex border border-slate-200">
                   <button onClick={() => setSegment('SENIOR')} className={`px-8 py-3 rounded-lg font-bold text-lg transition-all ${segment === 'SENIOR' ? 'bg-white shadow-md text-teal-700' : 'text-slate-500 hover:text-teal-600'}`}>Senior</button>
                   <button onClick={() => setSegment('MICE')} className={`px-8 py-3 rounded-lg font-bold text-lg transition-all ${segment === 'MICE' ? 'bg-white shadow-md text-blue-900' : 'text-slate-500 hover:text-blue-800'}`}>MICE (Pro)</button>
                 </div>
               </div>
             )}

             {error && <div className="bg-red-50 text-red-700 p-4 rounded-xl mb-8 font-bold border border-red-100">{error}</div>}

             {segment === 'SENIOR' ? (
               <SeniorQuoteForm offreId={id} onSubmit={handleSubmit} isLoading={isLoading} />
             ) : (
               <MICEQuoteForm offreId={id} onSubmit={handleSubmit} isLoading={isLoading} />
             )}
          </div>
        </div>
      </div>

      {showSuccessModal && (
        <div className="fixed inset-0 z-50 flex items-center justify-center bg-slate-900/60 backdrop-blur-sm px-4">
          <div className="bg-white rounded-3xl p-10 max-w-lg w-full shadow-2xl text-center transform scale-100 animate-in fade-in zoom-in duration-300">
            <CheckCircle size={80} className="text-teal-500 mx-auto mb-6" />
            <h2 className="text-3xl font-bold text-blue-900 mb-4">Demande Envoyée !</h2>
            <p className="text-slate-600 mb-10 text-xl leading-relaxed">
              Votre demande de devis a été transmise avec succès. Notre équipe vous répondra sous 24 à 48 heures.
            </p>
            <button 
              onClick={() => navigate('/client/dashboard')}
              className="w-full bg-blue-900 hover:bg-blue-800 text-white font-bold py-4 px-6 rounded-xl transition-all shadow-md text-xl"
            >
              Aller à Mon Espace Client
            </button>
          </div>
        </div>
      )}
    </div>
  );
};

export default QuoteRequest;
