using System;
using System.Diagnostics;
using System.Windows;

namespace patientApp
{
    public partial class VisitDetailsWindow : Window
    {
        private readonly Visit _visit;
        private readonly Patient _patient;
        
        public VisitDetailsWindow(Patient patient, Visit visit)
        {
            InitializeComponent();
            _visit = visit;
            _patient = patient;
            
            // Set patient information
            PatientNameText.Text = $"שם המטופל: {patient.Name}";
            PatientIdText.Text = $"תעודת זהות: {patient.Id}";
            
            // Set visit information
            VisitDateText.Text = $"תאריך ביקור: {visit.VisitDate:dd/MM/yyyy}";
            DescriptionText.Text = $"פרטים: {visit.Description}";
        }

        private void OpenPdfButton_Click(object sender, RoutedEventArgs e)
        {
            if (!string.IsNullOrEmpty(_visit.PdfFilePath))
            {
                try
                {
                    Process.Start(new ProcessStartInfo(_visit.PdfFilePath) { UseShellExecute = true });
                }
                catch (Exception ex)
                {
                    MessageBox.Show($"לא ניתן לפתוח קובץ PDF: {ex.Message}");
                }
            }
        }

        private void OpenDocButton_Click(object sender, RoutedEventArgs e)
        {
            if (!string.IsNullOrEmpty(_visit.DocFilePath))
            {
                try
                {
                    Process.Start(new ProcessStartInfo(_visit.DocFilePath) { UseShellExecute = true });
                }
                catch (Exception ex)
                {
                    MessageBox.Show($"לא ניתן לפתוח קובץ DOCX: {ex.Message}");
                }
            }
        }
    }
}
