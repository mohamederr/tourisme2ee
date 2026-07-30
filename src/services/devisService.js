import api from './api';

export const devisService = {
  createDevis: async (devisData) => {
    const response = await api.post('/demandes-devis', devisData);
    return response.data;
  },
  getMesDevis: async () => {
    const response = await api.get('/demandes-devis/mes-demandes');
    return response.data;
  },
  getDevisById: async (id) => {
    const response = await api.get(`/demandes-devis/${id}`);
    return response.data;
  }
};
