import React, { useState, useEffect } from 'react';
import { Link, useSearchParams } from 'react-router-dom';
import { offresService } from '../services/offresService';
import { Map, Users, Calendar, ArrowRight } from 'lucide-react';

const OffresCatalog = () => {
  const [searchParams, setSearchParams] = useSearchParams();
  const initialSegment = searchParams.get('segment') || 'ALL';
  const [activeTab, setActiveTab] = useState(initialSegment);
  const [offres, setOffres] = useState([]);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    const fetchOffres = async () => {
      setLoading(true);
      try {
        const segmentParam = activeTab === 'ALL' ? null : activeTab;
        const data = await offresService.getOffres(segmentParam);
        
        // Handle API result (Mocking it here if it fails, assuming it failed for demo)
        throw new Error("Force mock for UI demonstration");
      } catch (error) {
        // Fallback for UI Demo
        const allOffers = [
          { 
            id: 1, 
            titre: "Pack Oriental Senior", 
            description: "Séjour tout compris d'une semaine à un mois. Visites de l'Oriental (Oujda, Saïdia, Tafoughalt, Figuig). Pension complète et activités adaptées.", 
            segment: "SENIOR", 
            dureeMinJours: 7, 
            imageUrl: "https://images.unsplash.com/photo-1516483638261-f408892288b6?auto=format&fit=crop&q=80&w=800" 
          },
          { 
            id: 2, 
            titre: "Pack Oriental Pro/MICE", 
            description: "Séminaires et Teambuilding. Accès à la salle polyvalente équipée (Vidéoprojecteur, Sonorisation). Hébergement et restauration inclus.", 
            segment: "MICE", 
            dureeMinJours: 2, 
            imageUrl: "https://images.unsplash.com/photo-1540317580384-e5d43616b9aa?auto=format&fit=crop&q=80&w=800" 
          }
        ];
        
        if (activeTab === 'ALL') {
          setOffres(allOffers);
        } else {
          setOffres(allOffers.filter(o => o.segment === activeTab));
        }
      } finally {
        setLoading(false);
      }
    };
    
    fetchOffres();
    setSearchParams({ segment: activeTab });
  }, [activeTab, setSearchParams]);

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-10">
      <div className="text-center mb-12">
        <h2 className="text-5xl font-bold text-blue-900 mb-4">Notre Catalogue d'Offres</h2>
        <p className="text-2xl text-slate-600 max-w-3xl mx-auto leading-relaxed">
          Choisissez la formule adaptée à vos besoins au Centre d'Estivage ONCF, Saïdia.
        </p>
      </div>

      {/* Segment Tabs */}
      <div className="flex justify-center mb-16">
        <div className="bg-white rounded-full shadow-sm p-1.5 inline-flex border border-slate-200 flex-wrap justify-center gap-1">
          <button
            onClick={() => setActiveTab('ALL')}
            className={`px-8 py-3 rounded-full font-bold text-lg transition-all ${activeTab === 'ALL' ? 'bg-slate-800 text-white shadow-md' : 'text-slate-600 hover:text-slate-900 hover:bg-slate-50'}`}
          >
            Toutes nos offres
          </button>
          <button
            onClick={() => setActiveTab('SENIOR')}
            className={`px-8 py-3 rounded-full font-bold text-lg transition-all ${activeTab === 'SENIOR' ? 'bg-teal-600 text-white shadow-md' : 'text-slate-600 hover:text-teal-600 hover:bg-slate-50'}`}
          >
            Associations Seniors (Silver Economy)
          </button>
          <button
            onClick={() => setActiveTab('MICE')}
            className={`px-8 py-3 rounded-full font-bold text-lg transition-all ${activeTab === 'MICE' ? 'bg-blue-900 text-white shadow-md' : 'text-slate-600 hover:text-blue-900 hover:bg-slate-50'}`}
          >
            Entreprises & Séminaires (MICE)
          </button>
        </div>
      </div>

      {/* Grid */}
      {loading ? (
        <div className="flex justify-center py-32">
          <div className="animate-spin rounded-full h-16 w-16 border-t-4 border-b-4 border-teal-600"></div>
        </div>
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 gap-10">
          {offres.map((offre) => (
            <div key={offre.id} className="bg-white rounded-2xl overflow-hidden shadow-lg border border-slate-100 hover:shadow-2xl transition-all flex flex-col transform hover:-translate-y-1">
              <div className="h-72 overflow-hidden relative">
                <img src={offre.imageUrl} alt={offre.titre} className="w-full h-full object-cover transform hover:scale-105 transition-transform duration-700" />
                <div className="absolute top-6 right-6 bg-white/95 backdrop-blur-sm px-5 py-2 rounded-full text-base font-bold text-blue-900 shadow-md">
                  {offre.segment === 'SENIOR' ? 'Silver Economy' : 'MICE'}
                </div>
              </div>
              
              <div className="p-8 flex flex-col flex-grow">
                <h3 className="text-3xl font-bold text-blue-900 mb-4">{offre.titre}</h3>
                <p className="text-slate-600 mb-8 flex-grow text-lg leading-relaxed">
                  {offre.description}
                </p>
                
                <div className="space-y-4 mb-10">
                  <div className="flex items-center text-slate-700 gap-4">
                    <Calendar className="text-teal-600" size={24} />
                    <span className="font-medium text-lg">À partir de {offre.dureeMinJours} jours</span>
                  </div>
                  <div className="flex items-center text-slate-700 gap-4">
                    <Users className="text-teal-600" size={24} />
                    <span className="font-medium text-lg">Groupes & Collectivités</span>
                  </div>
                  <div className="flex items-center text-slate-700 gap-4">
                    <Map className="text-teal-600" size={24} />
                    <span className="font-medium text-lg">Saïdia & Région de l'Oriental</span>
                  </div>
                </div>

                <Link 
                  to={`/offer/${offre.id}`} 
                  className="w-full flex items-center justify-center gap-3 bg-slate-50 hover:bg-teal-50 text-teal-700 hover:text-teal-800 border-2 border-slate-200 hover:border-teal-300 font-bold py-4 px-6 rounded-xl transition-colors text-lg"
                >
                  Voir les détails & disponibilités
                  <ArrowRight size={24} />
                </Link>
              </div>
            </div>
          ))}
          {offres.length === 0 && (
            <div className="col-span-full text-center py-20 text-slate-500 text-2xl font-medium">
              Aucune offre disponible pour ce segment pour le moment.
            </div>
          )}
        </div>
      )}
    </div>
  );
};

export default OffresCatalog;
