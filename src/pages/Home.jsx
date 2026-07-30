import React from 'react';
import OffresCatalog from './OffresCatalog';
import { ShieldCheck, HeartHandshake, MapPin } from 'lucide-react';

const Home = () => {
  return (
    <div className="w-full bg-slate-50">
      {/* Hero Section */}
      <div className="relative h-[70vh] min-h-[600px] w-full flex items-center justify-center text-center">
        <div className="absolute inset-0">
          <img 
            src="https://images.unsplash.com/photo-1539667468225-eebb663053e6?auto=format&fit=crop&q=80&w=2000" 
            alt="Plage de Saïdia" 
            className="w-full h-full object-cover"
          />
          <div className="absolute inset-0 bg-blue-900/65 mix-blend-multiply"></div>
        </div>
        
        <div className="relative z-10 max-w-5xl mx-auto px-4 mt-8">
          <h1 className="text-5xl md:text-7xl font-bold text-white mb-6 drop-shadow-lg leading-tight">
            Vivez l'Exception au <br className="hidden md:block"/>
            <span className="text-teal-400">Centre ONCF Saïdia</span>
          </h1>
          <p className="text-xl md:text-2xl text-slate-100 mb-10 leading-relaxed font-medium drop-shadow-md max-w-3xl mx-auto">
            Des séjours adaptés pour les associations Seniors et des espaces entièrement équipés pour vos événements d'entreprise.
          </p>
          <div className="flex flex-wrap justify-center gap-4">
            <button 
              onClick={() => {
                document.getElementById('catalog-section').scrollIntoView({ behavior: 'smooth', block: 'start' });
              }}
              className="bg-teal-600 hover:bg-teal-500 text-white font-bold py-4 px-10 rounded-full transition-all shadow-xl text-xl transform hover:-translate-y-1"
            >
              Découvrir nos offres
            </button>
          </div>
        </div>
      </div>

      {/* Trust Badges */}
      <div className="bg-white py-12 border-b border-slate-200 shadow-lg relative z-20 -mt-16 mx-4 md:mx-auto max-w-6xl rounded-2xl">
        <div className="grid grid-cols-1 md:grid-cols-3 gap-10 px-10">
          <div className="flex flex-col items-center text-center space-y-4">
            <div className="w-20 h-20 bg-teal-50 text-teal-600 rounded-full flex items-center justify-center shadow-sm">
              <ShieldCheck size={40} />
            </div>
            <div>
              <h3 className="text-2xl font-bold text-blue-900 mb-2">Sécurité & Sérénité</h3>
              <p className="text-slate-600 text-lg leading-relaxed">
                Un centre sécurisé 24/7 avec assistance médicale sur site pour une tranquillité d'esprit totale (Silver Economy).
              </p>
            </div>
          </div>
          
          <div className="flex flex-col items-center text-center space-y-4">
            <div className="w-20 h-20 bg-blue-50 text-blue-900 rounded-full flex items-center justify-center shadow-sm">
              <HeartHandshake size={40} />
            </div>
            <div>
              <h3 className="text-2xl font-bold text-blue-900 mb-2">Service Tout Compris</h3>
              <p className="text-slate-600 text-lg leading-relaxed">
                Restauration adaptée, hébergement plain-pied et activités encadrées : tout est pensé pour votre confort.
              </p>
            </div>
          </div>

          <div className="flex flex-col items-center text-center space-y-4">
            <div className="w-20 h-20 bg-orange-50 text-orange-600 rounded-full flex items-center justify-center shadow-sm">
              <MapPin size={40} />
            </div>
            <div>
              <h3 className="text-2xl font-bold text-blue-900 mb-2">Localisation Idéale</h3>
              <p className="text-slate-600 text-lg leading-relaxed">
                Face à la Méditerranée, le point de départ parfait pour découvrir les merveilles de la région de l'Oriental.
              </p>
            </div>
          </div>
        </div>
      </div>

      {/* Embedded Catalog */}
      <div id="catalog-section" className="pt-8 pb-16">
        <OffresCatalog />
      </div>
    </div>
  );
};

export default Home;
