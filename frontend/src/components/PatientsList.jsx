import React, { useState, useEffect } from "react";
import { getAllPatients } from "../services/api";

const PatientsList = ({ onSelectPatient }) => {
    const [patients, setPatients] = useState([]);

    useEffect(() => {
        getAllPatients().then((response) => {
            setPatients(response.data);
        });
    }, []);

    return (
        <div>
            <h2>Patients</h2>
            <ul>
                {patients.map((patient) => (
                    <li key={patient.id} onClick={() => onSelectPatient(patient.id)}>
                        {patient.name}
                        {/*<a href={`/patients/${patient.id}`}>{patient.name}</a>*/}
                    </li>
                ))}
            </ul>
        </div>
    );
};



export default PatientsList;
