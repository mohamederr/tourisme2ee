import api from './api';

export const offresService = {
  // Get all offers, optionally filtered by segment (SENIOR or MICE)
  getOffres: async (segment) => {
    const params = segment ? { segment } : {};
    const response = await api.get('/offres', { params });
    return response.data;
  },
  
  // Get details for a specific offer
  getOffreById: async (id) => {
    const response = await api.get(`/offres/${id}`);
    return response.data;
  },

  // Get availability slots for the calendar
  getDisponibilites: async (offreId, mois, annee) => {
    const response = await api.get(`/disponibilites`, {
      params: { offreId, mois, annee }
    });
    return response.data;
  }
};
