import React, { useState, useEffect } from "react";
import { getVisitsByPatientId } from "../services/api";

const VisitsList = ({ patientId, onSelectVisit }) => {
    const [visits, setVisits] = useState([]);

    useEffect(() => {
        if (patientId) {
            getVisitsByPatientId(patientId).then((response) => {
                setVisits(response.data);
            });
        }
    }, [patientId]);

    return (
        <div>
            <h2>Visits</h2>
            <ul>
                {visits.map((visit) => (
                    <li key={visit.id} onClick={() => onSelectVisit(visit.id)}>
                        {visit.visitDate}
                    </li>
                ))}
            </ul>
        </div>
    );
};

export default VisitsList;
