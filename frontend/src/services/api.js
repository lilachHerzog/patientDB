import axios from "axios"

const API_URL = "http://localhost:8080/api";

export const getAllPatients = () => axios.get(`${API_URL}/patients`);
export const getVPatientByPatientId = (patientId) => axios.get(`${API_URL}/patients/${patientId}`);
export const getVisitsByPatientId = (patientId) => axios.get(`${API_URL}/patients/${patientId}/visits`);
export const getDocFilesByVisitId = (patientId, visitDate) => axios.get(`${API_URL}/patients/${patientId}/${visitDate}/docFiles`);
export const getPdfFilesByVisitId = (patientId, visitDate) => axios.get(`${API_URL}/patients/${patientId}/${visitDate}/pdfFiles`);
