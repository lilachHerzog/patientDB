namespace patientApp
{
    public class Parameters
    {
        public string AppTitle { get; set; } = "Patient App";
        // Add more global or patient-specific parameters as needed
    }

    public class Patient
    {
        public string Name { get; set; }
        public List<Visit> Visits { get; set; } = new List<Visit>();
    }

    public class Visit
    {
        public DateTime VisitDate { get; set; }
        public string Description { get; set; }
        public string PdfFilePath { get; set; }
        public string DocFilePath { get; set; }
    }
}
