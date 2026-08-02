import React, { useState, useEffect } from 'react';
import { offresService } from '../../services/offresService';
import { ChevronLeft, ChevronRight, Calendar as CalendarIcon } from 'lucide-react';

const AvailabilityCalendar = ({ offreId }) => {
  const [currentDate, setCurrentDate] = useState(new Date());
  const [disponibilites, setDisponibilites] = useState([]);
  const [loading, setLoading] = useState(false);

  const fetchDisponibilites = async () => {
    setLoading(true);
    try {
      const mois = currentDate.getMonth() + 1;
      const annee = currentDate.getFullYear();
      const data = await offresService.getDisponibilites(offreId, mois, annee);
      setDisponibilites(data);
    } catch (error) {
      console.error("Erreur de chargement des disponibilités, utilisation des données de démonstration.", error);
      // Fallback Demo Data if Backend isn't ready
      const year = currentDate.getFullYear();
      const month = String(currentDate.getMonth() + 1).padStart(2, '0');
      setDisponibilites([
        { date: `${year}-${month}-12`, placesRestantes: 20, statut: 'DISPONIBLE' },
        { date: `${year}-${month}-13`, placesRestantes: 18, statut: 'DISPONIBLE' },
        { date: `${year}-${month}-14`, placesRestantes: 0, statut: 'COMPLET' },
        { date: `${year}-${month}-15`, placesRestantes: 0, statut: 'COMPLET' },
        { date: `${year}-${month}-22`, placesRestantes: 4, statut: 'LIMITE' },
        { date: `${year}-${month}-23`, placesRestantes: 5, statut: 'LIMITE' },
      ]);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    if (offreId) {
      fetchDisponibilites();
    }
  }, [currentDate, offreId]);

  const handlePrevMonth = () => setCurrentDate(new Date(currentDate.getFullYear(), currentDate.getMonth() - 1, 1));
  const handleNextMonth = () => setCurrentDate(new Date(currentDate.getFullYear(), currentDate.getMonth() + 1, 1));

  const getDaysInMonth = (year, month) => new Date(year, month + 1, 0).getDate();
  const getFirstDayOfMonth = (year, month) => {
    const day = new Date(year, month, 1).getDay();
    return day === 0 ? 6 : day - 1; // Start on Monday
  };

  const daysInMonth = getDaysInMonth(currentDate.getFullYear(), currentDate.getMonth());
  const firstDay = getFirstDayOfMonth(currentDate.getFullYear(), currentDate.getMonth());
  
  const monthNames = ["Janvier", "Février", "Mars", "Avril", "Mai", "Juin", "Juillet", "Août", "Septembre", "Octobre", "Novembre", "Décembre"];
  const dayNames = ["Lun", "Mar", "Mer", "Jeu", "Ven", "Sam", "Dim"];

  const renderDays = () => {
    const days = [];
    for (let i = 0; i < firstDay; i++) {
      days.push(<div key={`empty-${i}`} className="h-24 border border-slate-100 bg-slate-50 opacity-50"></div>);
    }
    for (let d = 1; d <= daysInMonth; d++) {
      const dateStr = `${currentDate.getFullYear()}-${String(currentDate.getMonth()+1).padStart(2, '0')}-${String(d).padStart(2, '0')}`;
      const dispo = disponibilites.find(item => item.date === dateStr);
      
      let bgColor = "bg-white hover:bg-slate-50";
      let statusBadge = null;

      if (dispo) {
        if (dispo.statut === 'COMPLET' || dispo.placesRestantes === 0) {
          bgColor = "bg-red-50";
          statusBadge = <span className="text-xs font-bold text-red-700 bg-red-100 px-2 py-1 rounded w-full text-center">Complet</span>;
        } else if (dispo.statut === 'LIMITE' || dispo.placesRestantes <= 5) {
          bgColor = "bg-orange-50";
          statusBadge = <span className="text-xs font-bold text-orange-700 bg-orange-100 px-2 py-1 rounded w-full text-center">{dispo.placesRestantes} places</span>;
        } else {
          bgColor = "bg-teal-50";
          statusBadge = <span className="text-xs font-bold text-teal-700 bg-teal-100 px-2 py-1 rounded w-full text-center">{dispo.placesRestantes} places</span>;
        }
      }

      days.push(
        <div key={d} className={`h-24 border border-slate-200 p-2 flex flex-col justify-between transition-colors ${bgColor}`}>
          <span className="text-slate-700 font-semibold text-lg">{d}</span>
          <div className="mt-auto flex justify-center w-full">
             {statusBadge}
          </div>
        </div>
      );
    }
    return days;
  };

  return (
    <div className="bg-white rounded-2xl shadow-sm border border-slate-200 p-8">
      <div className="flex items-center justify-between mb-8">
        <h3 className="text-2xl font-bold text-blue-900 flex items-center gap-3">
          <CalendarIcon className="text-teal-600" size={28} />
          Calendrier des Disponibilités
        </h3>
        <div className="flex items-center gap-4 bg-slate-50 rounded-full p-1 border border-slate-200">
          <button onClick={handlePrevMonth} className="p-2 rounded-full hover:bg-white hover:shadow-sm transition-all text-slate-600">
            <ChevronLeft size={24} />
          </button>
          <span className="font-bold text-lg text-slate-800 w-44 text-center capitalize">
            {monthNames[currentDate.getMonth()]} {currentDate.getFullYear()}
          </span>
          <button onClick={handleNextMonth} className="p-2 rounded-full hover:bg-white hover:shadow-sm transition-all text-slate-600">
            <ChevronRight size={24} />
          </button>
        </div>
      </div>
      
      {/* Légende Accessibility/Legibility */}
      <div className="flex gap-6 mb-6 text-base font-medium">
        <div className="flex items-center gap-2"><span className="w-4 h-4 rounded-full bg-teal-500 shadow-sm"></span><span className="text-slate-700">Disponible</span></div>
        <div className="flex items-center gap-2"><span className="w-4 h-4 rounded-full bg-orange-400 shadow-sm"></span><span className="text-slate-700">Places limitées</span></div>
        <div className="flex items-center gap-2"><span className="w-4 h-4 rounded-full bg-red-500 shadow-sm"></span><span className="text-slate-700">Complet</span></div>
      </div>

      {loading ? (
        <div className="h-96 flex items-center justify-center">
          <div className="animate-spin rounded-full h-16 w-16 border-t-4 border-b-4 border-teal-600"></div>
        </div>
      ) : (
        <div className="border border-slate-200 rounded-xl overflow-hidden shadow-sm">
          <div className="grid grid-cols-7 bg-slate-100 border-b border-slate-200">
            {dayNames.map(day => (
              <div key={day} className="py-4 text-center font-bold text-slate-700 text-sm uppercase tracking-widest">
                {day}
              </div>
            ))}
          </div>
          <div className="grid grid-cols-7">
            {renderDays()}
          </div>
        </div>
      )}
    </div>
  );
};

export default AvailabilityCalendar;
