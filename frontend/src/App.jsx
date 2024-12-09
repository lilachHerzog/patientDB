import React, { useState, useEffect } from 'react';
import axios from "axios"
import { getDocFilesByVisitId, getPdfFilesByVisitId } from './services/api';

const App = () => {
    const [patients, setPatients] = useState([]);
    const [selectedPatient, setSelectedPatient] = useState(null);
    const [selectedVisitDate, setSelectedVisitDate] = useState(null);
    const [docFiles, setDocFiles] = useState([]);
    const [pdfFiles, setPdfFiles] = useState([]);

    const formatDate = (dateString) => {
        const date = new Date(dateString);
        const day = String(date.getDate()).padStart(2, '0'); // מוסיף אפס אם היום הוא חד ספרתי
        const month = String(date.getMonth() + 1).padStart(2, '0'); // מוסיף אפס אם החודש הוא חד ספרתי
        const year = String(date.getFullYear()).slice(-2); // שומר רק את 2 הספרות האחרונות של השנה

        return `${day}${month}${year}`;
    };

    const formattedDate = formatDate('2024-12-09'); // דוגמה
    console.log(formattedDate); // "091224"


    // פונקציה שמביאה את המטופלים
    useEffect(() => {
        // קריאה ל-API להורדת המטופלים (אם יש API לכך)
        axios.get('http://localhost:8080/api/patients')
            .then(response => {
                setPatients(response.data);
            })
            .catch(error => {
                console.error("There was an error fetching the patients!", error);
            });
    }, []);

    // פונקציה שמביאה את הקבצים עבור ביקור מסוים
    const onSelectVisit = (visitDate) => {
        setSelectedVisitDate(visitDate);

        // המרת התאריך לפורמט ddMMyy
        const formattedDate = formatDate(visitDate);

        // קריאה ל-API לקבלת קבצי doc
        getDocFilesByVisitId(selectedPatient.id, formattedDate)
            .then(response => {
                setDocFiles(response.data);
            })
            .catch(error => {
                console.error("There was an error fetching doc files!", error);
            });

        // קריאה ל-API לקבלת קבצי pdf
        getPdfFilesByVisitId(selectedPatient.id, formattedDate)
            .then(response => {
                setPdfFiles(response.data);
            })
            .catch(error => {
                console.error("There was an error fetching pdf files!", error);
            });
    };


    return (
        <div>
            {/* רשימת המטופלים */}
            <ul>
                {patients.map(patient => (
                    <li key={patient.id} onClick={() => setSelectedPatient(patient)}>
                        {patient.name}
                    </li>
                ))}
            </ul>

            {/* הצגת הביקורים של המטופל שנבחר */}
            {selectedPatient && (
                <div>
                    <h2>Visits for {selectedPatient.name}</h2>
                    <ul>
                        {selectedPatient.visits.map(visit => (
                            <li key={visit.id} onClick={() => onSelectVisit(visit.visitDate)}>
                                {visit.visitDate}
                            </li>
                        ))}
                    </ul>
                </div>
            )}

            {/* הצגת הקבצים של הביקור */}
            {selectedVisitDate && (
                <div>
                    <h3>Doc Files for visit on {selectedVisitDate}</h3>
                    <ul>
                        {docFiles.map(file => (
                            <li key={file.filename}>{file.filename}</li>
                        ))}
                    </ul>

                    <h3>Pdf Files for visit on {selectedVisitDate}</h3>
                    <ul>
                        {pdfFiles.map(file => (
                            <li key={file.filename}>{file.filename}</li>
                        ))}
                    </ul>
                </div>
            )}
        </div>
    );
};

export default App;
