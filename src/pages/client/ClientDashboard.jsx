import React, { useState, useEffect } from 'react';
import { devisService } from '../../services/devisService';
import { FileText, Download, Eye, Clock, CheckCircle2, XCircle, AlertCircle } from 'lucide-react';

const StatusBadge = ({ statut }) => {
  switch (statut) {
    case 'EN_ATTENTE':
      return <span className="inline-flex items-center gap-2 px-4 py-1.5 rounded-full text-base font-bold bg-yellow-100 text-yellow-800 border border-yellow-200"><Clock size={18} /> En attente</span>;
    case 'TRAITE':
      return <span className="inline-flex items-center gap-2 px-4 py-1.5 rounded-full text-base font-bold bg-blue-100 text-blue-800 border border-blue-200"><AlertCircle size={18} /> Devis Disponible</span>;
    case 'CONFIRME':
      return <span className="inline-flex items-center gap-2 px-4 py-1.5 rounded-full text-base font-bold bg-green-100 text-green-800 border border-green-200"><CheckCircle2 size={18} /> Confirmé</span>;
    case 'REFUSE':
      return <span className="inline-flex items-center gap-2 px-4 py-1.5 rounded-full text-base font-bold bg-red-100 text-red-800 border border-red-200"><XCircle size={18} /> Refusé</span>;
    default:
      return <span className="inline-flex items-center px-4 py-1.5 rounded-full text-base font-bold bg-slate-100 text-slate-800 border border-slate-200">{statut}</span>;
  }
};

const ClientDashboard = () => {
  const [devisList, setDevisList] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchDevis = async () => {
      try {
        const data = await devisService.getMesDevis();
        setDevisList(data);
      } catch (error) {
        // UI fallback mockup data
        setDevisList([
          { id: 104, offreId: 1, dateSouhaitee: '2026-09-15', nbParticipants: 20, statutDevis: 'EN_ATTENTE', createdAt: '2026-07-30T10:00:00Z', segment: 'SENIOR' },
          { id: 102, offreId: 2, dateSouhaitee: '2026-10-05', nbParticipants: 50, statutDevis: 'TRAITE', createdAt: '2026-07-28T14:30:00Z', segment: 'MICE' },
          { id: 95, offreId: 1, dateSouhaitee: '2026-05-10', nbParticipants: 18, statutDevis: 'CONFIRME', createdAt: '2026-04-15T09:15:00Z', segment: 'SENIOR' }
        ]);
      } finally {
        setLoading(false);
      }
    };
    fetchDevis();
  }, []);

  return (
    <div>
      <div className="mb-10 pb-6 border-b border-slate-200">
        <h1 className="text-4xl font-bold text-blue-900 tracking-tight">Mes Demandes de Devis</h1>
        <p className="text-slate-600 mt-3 text-xl">Suivez l'état de vos réservations, téléchargez vos devis et factures.</p>
      </div>

      {loading ? (
        <div className="flex justify-center py-20">
          <div className="animate-spin rounded-full h-16 w-16 border-t-4 border-b-4 border-teal-600"></div>
        </div>
      ) : devisList.length === 0 ? (
        <div className="text-center py-20 bg-slate-50 rounded-2xl border border-slate-200">
          <FileText size={64} className="mx-auto text-slate-300 mb-6" />
          <h3 className="text-2xl font-bold text-slate-700 mb-3">Aucune demande active</h3>
          <p className="text-slate-500 text-lg">Vous n'avez pas encore effectué de demande de devis.</p>
        </div>
      ) : (
        <div className="space-y-6">
          {devisList.map((devis) => (
            <div key={devis.id} className="bg-white p-8 rounded-2xl border border-slate-200 shadow-sm flex flex-col xl:flex-row xl:items-center justify-between gap-8 hover:shadow-md hover:border-teal-200 transition-all">
              <div className="flex-grow">
                <div className="flex items-center gap-4 mb-4">
                  <span className="text-sm font-black px-3 py-1 bg-slate-100 text-slate-500 rounded-md uppercase tracking-wider">#{devis.id}</span>
                  <h3 className="font-bold text-2xl text-blue-900">
                    {devis.segment === 'SENIOR' ? 'Pack Oriental Senior' : 'Pack Oriental Pro/MICE'}
                  </h3>
                </div>
                <div className="flex flex-wrap gap-x-8 gap-y-3 text-lg text-slate-600">
                  <span className="flex items-center gap-2"><strong className="text-slate-800">Date Prévue:</strong> {devis.dateSouhaitee}</span>
                  <span className="flex items-center gap-2"><strong className="text-slate-800">Groupe:</strong> {devis.nbParticipants} pers.</span>
                  <span className="flex items-center gap-2"><strong className="text-slate-800">Déposé le:</strong> {new Date(devis.createdAt).toLocaleDateString('fr-FR')}</span>
                </div>
              </div>
              
              <div className="flex flex-col xl:items-end gap-4 min-w-[200px]">
                <StatusBadge statut={devis.statutDevis} />
                
                {devis.statutDevis === 'TRAITE' && (
                  <button className="flex items-center justify-center gap-2 px-5 py-2.5 rounded-xl text-base font-bold bg-teal-50 text-teal-700 hover:bg-teal-600 hover:text-white border border-teal-200 transition-all mt-2 w-full xl:w-auto">
                    <Download size={20} /> Télécharger l'Offre
                  </button>
                )}
                {devis.statutDevis === 'CONFIRME' && (
                  <button className="flex items-center justify-center gap-2 px-5 py-2.5 rounded-xl text-base font-bold bg-slate-50 text-slate-700 hover:bg-blue-900 hover:text-white border border-slate-200 transition-all mt-2 w-full xl:w-auto">
                    <Eye size={20} /> Voir Facture
                  </button>
                )}
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
};

export default ClientDashboard;
