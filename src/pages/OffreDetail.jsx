import React, { useState, useEffect } from 'react';
import { useParams, Link } from 'react-router-dom';
import { offresService } from '../services/offresService';
import AvailabilityCalendar from '../components/calendar/AvailabilityCalendar';
import { MapPin, Users, Calendar, CheckCircle2, ArrowRight, Info, Presentation } from 'lucide-react';

const OffreDetail = () => {
  const { id } = useParams();
  const [offre, setOffre] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchOffre = async () => {
      try {
        const data = await offresService.getOffreById(id);
        setOffre(data);
      } catch (error) {
        // Mock data based on ID for demo/development purposes
        if (id === '1') {
          setOffre({
            id: 1,
            titre: "Pack Oriental Senior",
            segment: "SENIOR",
            description: "Un séjour conçu spécialement pour les associations de retraités. Découvrez les merveilles de l'Oriental marocain dans un cadre sécurisant et serein au bord de la Méditerranée.",
            imageUrl: "https://images.unsplash.com/photo-1516483638261-f408892288b6?auto=format&fit=crop&q=80&w=1200",
            programme: [
              { jour: 1, titre: "Arrivée et Installation", desc: "Accueil VIP au Centre ONCF de Saïdia. Dîner traditionnel et repos." },
              { jour: 2, titre: "Découverte de Saïdia", desc: "Balade guidée sur la Marina et corniche de Saïdia. Temps libre et activités douces l'après-midi." },
              { jour: 3, titre: "Excursion à Oujda", desc: "Visite de l'ancienne Médina d'Oujda, découverte des souks traditionnels, de l'architecture locale et déjeuner typique." },
              { jour: 4, titre: "Tafoughalt et Nature", desc: "Découverte des grottes du chameau et de la beauté verdoyante des montagnes de Beni Snassen." },
              { jour: 5, titre: "L'Oasis de Figuig", desc: "Départ pour explorer les palmeraies historiques (inclus dans les options longue durée de 2+ semaines)." }
            ],
            inclus: ["Hébergement en pavillon de plain-pied (Accessibilité PMR)", "Pension complète (Menus adaptés)", "Transport touristique climatisé", "Accompagnement médical 24/7 sur site", "Activités adaptées (Gym douce, soirées animées)"]
          });
        } else {
          setOffre({
            id: 2,
            titre: "Pack Oriental Pro/MICE",
            segment: "MICE",
            description: "Offrez à votre entreprise un cadre exceptionnel pour vos séminaires, congrès et teambuilding. Alliez travail et détente au Centre ONCF Saïdia.",
            imageUrl: "https://images.unsplash.com/photo-1540317580384-e5d43616b9aa?auto=format&fit=crop&q=80&w=1200",
            salleSpecs: {
              capacite: "Jusqu'à 150 personnes en style théâtre",
              equipements: ["Vidéoprojecteur 4K Haute Luminosité", "Sonorisation professionnelle & Micros HF", "Paperboard & Fournitures", "Wi-Fi Haut Débit (Fibre)"],
              disposition: "Théâtre, U, École, Banquet, Cabaret"
            },
            inclus: ["Accès exclusif à la salle polyvalente climatisée", "Pauses café et restauration sur mesure (Buffet/Service à table)", "Hébergement en pavillon VIP pour cadres", "Activités de teambuilding (Sports nautiques, Golf à proximité)", "Assistance technique dédiée durant le séminaire"]
          });
        }
      } finally {
        setLoading(false);
      }
    };
    
    fetchOffre();
  }, [id]);

  if (loading) return (
    <div className="min-h-screen flex items-center justify-center bg-slate-50">
      <div className="animate-spin rounded-full h-16 w-16 border-t-4 border-b-4 border-teal-600"></div>
    </div>
  );

  if (!offre) return <div className="min-h-screen flex items-center justify-center text-3xl font-medium text-slate-600 bg-slate-50">Offre introuvable.</div>;

  return (
    <div className="bg-slate-50 min-h-screen pb-24">
      {/* Hero Header */}
      <div className="relative h-[450px] w-full">
        <img src={offre.imageUrl} alt={offre.titre} className="w-full h-full object-cover" />
        <div className="absolute inset-0 bg-blue-900/70 mix-blend-multiply"></div>
        <div className="absolute inset-0 flex items-center">
          <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 w-full mt-10">
            <span className="inline-block px-5 py-2 rounded-full bg-teal-500 text-white font-bold text-sm md:text-base tracking-wide mb-6 shadow-md">
              {offre.segment === 'SENIOR' ? 'Silver Economy - Associations' : 'MICE - Séminaires & Pro'}
            </span>
            <h1 className="text-4xl md:text-6xl font-bold text-white mb-6 leading-tight drop-shadow-lg">
              {offre.titre}
            </h1>
            <p className="text-xl md:text-2xl text-slate-100 max-w-3xl leading-relaxed drop-shadow-md">
              {offre.description}
            </p>
          </div>
        </div>
      </div>

      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 -mt-16 relative z-10">
        <div className="grid grid-cols-1 lg:grid-cols-3 gap-10">
          
          {/* Main Content Area */}
          <div className="lg:col-span-2 space-y-10">
            
            <div className="bg-white rounded-2xl shadow-md border border-slate-200 p-8 md:p-10">
              
              {/* Senior Program */}
              {offre.segment === 'SENIOR' && offre.programme && (
                <div>
                  <h2 className="text-3xl font-bold text-blue-900 mb-8 flex items-center gap-3">
                    <MapPin className="text-teal-600" size={32} />
                    Programme Type du Circuit
                  </h2>
                  <div className="space-y-8">
                    {offre.programme.map((item, index) => (
                      <div key={index} className="flex gap-6">
                        <div className="flex flex-col items-center">
                          <div className="w-14 h-14 rounded-full bg-teal-50 flex items-center justify-center text-teal-700 font-extrabold text-xl shadow-inner border border-teal-100">
                            J{item.jour}
                          </div>
                          {index !== offre.programme.length - 1 && <div className="w-1 h-full bg-teal-50 mt-3 rounded-full"></div>}
                        </div>
                        <div className="pb-6">
                          <h4 className="text-xl font-bold text-slate-800 mb-2">{item.titre}</h4>
                          <p className="text-slate-600 text-lg leading-relaxed">{item.desc}</p>
                        </div>
                      </div>
                    ))}
                  </div>
                </div>
              )}

              {/* MICE Specs */}
              {offre.segment === 'MICE' && offre.salleSpecs && (
                <div>
                  <h2 className="text-3xl font-bold text-blue-900 mb-8 flex items-center gap-3">
                    <Presentation className="text-teal-600" size={32} />
                    Spécifications Salle Polyvalente
                  </h2>
                  <div className="grid grid-cols-1 md:grid-cols-2 gap-8 bg-slate-50 p-8 rounded-2xl border border-slate-100">
                    <div>
                      <h4 className="font-bold text-slate-800 mb-4 text-xl">Capacité & Disposition</h4>
                      <div className="bg-white p-4 rounded-xl shadow-sm mb-4">
                        <p className="text-slate-700 font-medium text-lg mb-1">Capacité Max</p>
                        <p className="text-teal-700 font-bold">{offre.salleSpecs.capacite}</p>
                      </div>
                      <div className="bg-white p-4 rounded-xl shadow-sm">
                        <p className="text-slate-700 font-medium text-lg mb-1">Dispositions Possibles</p>
                        <p className="text-slate-600">{offre.salleSpecs.disposition}</p>
                      </div>
                    </div>
                    <div>
                      <h4 className="font-bold text-slate-800 mb-4 text-xl">Équipements Inclus</h4>
                      <ul className="space-y-4">
                        {offre.salleSpecs.equipements.map((eq, i) => (
                          <li key={i} className="flex items-start gap-3 bg-white p-3 rounded-xl shadow-sm text-slate-700 font-medium">
                            <CheckCircle2 size={24} className="text-teal-500 flex-shrink-0" />
                            <span>{eq}</span>
                          </li>
                        ))}
                      </ul>
                    </div>
                  </div>
                </div>
              )}
            </div>

            {/* Inclusions */}
            <div className="bg-white rounded-2xl shadow-md border border-slate-200 p-8 md:p-10">
              <h2 className="text-3xl font-bold text-blue-900 mb-8">Ce qui est inclus dans l'offre</h2>
              <div className="grid grid-cols-1 sm:grid-cols-2 gap-6">
                {offre.inclus?.map((item, index) => (
                  <div key={index} className="flex items-center gap-4 p-4 rounded-xl bg-slate-50 border border-slate-100 hover:border-teal-200 transition-colors">
                    <CheckCircle2 className="text-teal-500 flex-shrink-0" size={28} />
                    <span className="text-slate-800 font-medium text-lg leading-snug">{item}</span>
                  </div>
                ))}
              </div>
            </div>
            
            {/* Embedded Calendar Component */}
            <AvailabilityCalendar offreId={offre.id} />
            
          </div>

          {/* Sticky Sidebar CTA */}
          <div className="lg:col-span-1">
            <div className="bg-white rounded-2xl shadow-xl border-t-8 border-teal-600 p-8 sticky top-28">
              <h3 className="text-3xl font-bold text-blue-900 mb-6">Réserver cette offre</h3>
              <p className="text-slate-600 mb-8 text-lg leading-relaxed font-medium">
                {offre.segment === 'SENIOR' 
                  ? "Obtenez un devis sur mesure pour votre groupe. Accessibilité et sécurité sont nos priorités."
                  : "Organisez votre séminaire en toute sérénité. Demandez un devis pour bloquer la salle et les pavillons."}
              </p>
              
              <Link 
                to={`/quote/${offre.id}`}
                className="w-full flex items-center justify-center gap-3 bg-teal-600 hover:bg-teal-500 text-white font-bold py-5 px-6 rounded-xl transition-all shadow-lg hover:shadow-xl transform hover:-translate-y-1 text-xl"
              >
                Demander un devis
                <ArrowRight size={24} />
              </Link>
              
              <div className="mt-8 pt-6 border-t border-slate-100">
                <div className="flex items-center justify-center gap-3 text-slate-500 text-base font-medium">
                  <Info size={20} className="text-teal-600" />
                  Devis 100% gratuit, réponse sous 24h.
                </div>
              </div>
            </div>
          </div>

        </div>
      </div>
    </div>
  );
};

export default OffreDetail;
