import React from 'react';
import { Link } from 'react-router-dom';
import { MapPin, Phone, Mail } from 'lucide-react';

const Footer = () => {
  return (
    <footer className="bg-slate-900 text-slate-300 py-12 border-t-4 border-teal-600">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="grid grid-cols-1 md:grid-cols-3 gap-10">
          
          {/* Brand & Description */}
          <div className="flex flex-col space-y-4">
            <span className="font-bold text-2xl text-white tracking-wide">TOURISME 2E S.A.R.L.</span>
            <p className="text-slate-400 text-base leading-relaxed max-w-sm">
              Votre partenaire de confiance pour les séjours de groupes "Silver Economy" et événements d'entreprise (MICE) au Centre d'Estivage ONCF, Saïdia.
            </p>
          </div>

          {/* Quick Links */}
          <div className="flex flex-col space-y-4">
            <h3 className="text-xl font-semibold text-white">Liens Rapides</h3>
            <ul className="space-y-3 text-[17px]">
              <li>
                <Link to="/catalog?segment=SENIOR" className="hover:text-teal-400 transition-colors flex items-center gap-2">
                  <span className="w-2 h-2 rounded-full bg-teal-500"></span>
                  Pack Oriental Senior
                </Link>
              </li>
              <li>
                <Link to="/catalog?segment=MICE" className="hover:text-teal-400 transition-colors flex items-center gap-2">
                  <span className="w-2 h-2 rounded-full bg-teal-500"></span>
                  Pack Oriental Pro/MICE
                </Link>
              </li>
              <li>
                <Link to="/login" className="hover:text-teal-400 transition-colors flex items-center gap-2">
                  <span className="w-2 h-2 rounded-full bg-teal-500"></span>
                  Espace Client
                </Link>
              </li>
            </ul>
          </div>

          {/* Contact Information */}
          <div className="flex flex-col space-y-4">
            <h3 className="text-xl font-semibold text-white">Contact</h3>
            <ul className="space-y-4 text-[17px]">
              <li className="flex items-start gap-4">
                <MapPin className="text-teal-500 mt-1 flex-shrink-0" size={24} />
                <span>Centre d'Estivage ONCF<br/>Boulevard Front de Mer, Saïdia<br/>Maroc</span>
              </li>
              <li className="flex items-center gap-4">
                <Phone className="text-teal-500 flex-shrink-0" size={24} />
                <span className="font-medium">+212 5 00 00 00 00</span>
              </li>
              <li className="flex items-center gap-4">
                <Mail className="text-teal-500 flex-shrink-0" size={24} />
                <span>contact@tourisme2e.ma</span>
              </li>
            </ul>
          </div>
          
        </div>
        
        <div className="border-t border-slate-800 mt-12 pt-8 text-center text-base text-slate-500">
          <p>&copy; {new Date().getFullYear()} Tourisme 2E S.A.R.L. Tous droits réservés.</p>
        </div>
      </div>
    </footer>
  );
};

export default Footer;
