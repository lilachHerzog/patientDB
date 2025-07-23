namespace patientApp
{
    public class Parameters
    {
        public string AppTitle { get; set; } = "Patient App";
        // Add more global or patient-specific parameters as needed
    }

    public class Patient
    {
        public string Id { get; set; } = string.Empty;
        public string Name { get; set; } = string.Empty;
        public List<Visit> Visits { get; set; } = new List<Visit>();
    }

    public class Visit
    {
        public DateTime VisitDate { get; set; }
        public string Description { get; set; } = string.Empty;
        public string? PdfFilePath { get; set; }
        public string? DocFilePath { get; set; }
    }
}
