const onSelectPatient = (patientId) => {
    const [visits, setVisits] = useState([]);  // אחסון הביקורים של המטופל

    // קריאה ל-API כדי לקבל את הביקורים של המטופל לפי ה-id
    axios.get(`http://localhost:8080/api/patients/${patientId}/visits`)
        .then(response => {
            setVisits(response.data);  // עדכון מצב הביקורים במטופל
        })
        .catch(error => {
            console.error("There was an error fetching the visits!", error);
        });
    return (
        <div>
            <ul>
                {patients.map(patient => (
                    <li key={patient.id} onClick={() => onSelectPatient(patient.id)}>
                        {patient.name}
                    </li>
                ))}
            </ul>

            <div>
                <h2>Visits for {selectedPatient.name}</h2>
                <ul>
                    {visits.map(visit => (
                        <li key={visit.id}>
                            Visit Date: {visit.visitDate}
                            {/* תוכל להוסיף כאן גם את הקבצים של הביקור אם יש */}
                        </li>
                    ))}
                </ul>
            </div>
        </div>
    );

};
