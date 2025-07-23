using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Windows;
using System.Windows.Controls;
using patientApp;

namespace patientApp
{
    /// <summary>
    /// Interaction logic for MainWindow.xaml
    /// </summary>
    public partial class MainWindow : Window
    {
        private List<Patient> patients = new List<Patient>();
        private readonly List<Patient> allPatients = new List<Patient>(); // Store all patients for filtering

        public MainWindow()
        {
            InitializeComponent();
            LoadPatients();
            allPatients.AddRange(patients); // Store all patients
            PatientsListBox.ItemsSource = patients;
            VisitsDataGrid.MouseDoubleClick += VisitsDataGrid_MouseDoubleClick;
            VisitsDataGrid.LoadingRow += VisitsDataGrid_LoadingRow;
        }

        private void VisitsDataGrid_LoadingRow(object? sender, DataGridRowEventArgs e)
        {
            if (e.Row.GetIndex() == 0)
            {
                // Skip the header row
                return;
            }
            // Add 1 to make it 1-based index
            e.Row.Header = (e.Row.GetIndex() + 1).ToString();
        }

        private void VisitsDataGrid_MouseDoubleClick(object sender, System.Windows.Input.MouseButtonEventArgs e)
        {
            if (VisitsDataGrid.SelectedItem is Visit selectedVisit)
            {
                var detailsWindow = new VisitDetailsWindow(selectedVisit);
                detailsWindow.Owner = this;
                detailsWindow.ShowDialog();
            }
        }

        private void LoadPatients()
        {
            allPatients.Clear();
            patients.Clear();
            
            var samplePatients = new List<Patient>
            {
                new Patient
                {
                    Name = "ג'ון דוא",
                    Id = "123456789",
                    Visits = new List<Visit>
                    {
                        new Visit {
                            VisitDate = DateTime.Now.AddDays(-10),
                            Description = "בדיקה רפואית",
                            PdfFilePath = "C:/Files/JohnDoe_AnnualCheckup.pdf",
                            DocFilePath = "C:/Files/JohnDoe_AnnualCheckup.docx"
                        },
                        new Visit {
                            VisitDate = DateTime.Now.AddDays(-5),
                            Description = "מעקב רפואי",
                            PdfFilePath = "C:/Files/JohnDoe_Followup.pdf",
                            DocFilePath = "C:/Files/JohnDoe_Followup.docx"
                        }
                    }
                },
                new Patient
                {
                    Name = "ג'יין סמיט",
                    Id = "298765431",
                    Visits = new List<Visit>
                    {
                        new Visit {
                            VisitDate = DateTime.Now.AddDays(-20),
                            Description = "ייעוץ רפואי",
                            PdfFilePath = "C:/Files/JaneSmith_Consultation.pdf",
                            DocFilePath = "C:/Files/JaneSmith_Consultation.docx"
                        }
                    }
                }
            };
            
            // Initialize patients list with all patients (no duplicates)
            allPatients.Clear();
            patients.Clear();
            allPatients.AddRange(samplePatients);
            patients.AddRange(samplePatients);
        }

        private void PatientsListBox_SelectionChanged(object sender, SelectionChangedEventArgs e)
        {
            var selectedPatient = PatientsListBox.SelectedItem as Patient;
            if (selectedPatient != null)
            {
                VisitsDataGrid.ItemsSource = selectedPatient.Visits;
            }
            else
            {
                VisitsDataGrid.ItemsSource = null;
            }
        }

        private void SearchTextBox_TextChanged(object sender, TextChangedEventArgs e)
        {
            if (SearchTextBox == null || PatientsListBox == null) return;
            
            string searchText = SearchTextBox.Text.Trim();
            
            // If the search box contains the default text or is empty, show all patients
            if (searchText == "חפש לפי שם או תעודת זהות" || string.IsNullOrWhiteSpace(searchText))
            {
                // Clear and repopulate to avoid duplicates
                var currentSelection = PatientsListBox.SelectedItem;
                patients.Clear();
                patients.AddRange(allPatients
                    .GroupBy(p => p.Id)
                    .Select(g => g.First()));
                
                // Restore selection if still valid
                if (currentSelection is Patient selectedPatient && patients.Contains(selectedPatient))
                {
                    PatientsListBox.SelectedItem = selectedPatient;
                }
            }
            else
            {
                // Filter patients by name or ID (case-insensitive)
                searchText = searchText.ToLower();
                var filteredPatients = allPatients
                    .Where(p => (p.Name?.ToLower().Contains(searchText) ?? false) || 
                              (p.Id?.Contains(searchText, StringComparison.OrdinalIgnoreCase) ?? false))
                    .GroupBy(p => p.Id)  // Group by ID to remove duplicates
                    .Select(g => g.First())
                    .ToList();
                
                var currentSelection = PatientsListBox.SelectedItem;
                patients.Clear();
                patients.AddRange(filteredPatients);
                
                // Restore selection if still valid
                if (currentSelection is Patient selectedPatient && patients.Contains(selectedPatient))
                {
                    PatientsListBox.SelectedItem = selectedPatient;
                }
            }
            
            // Update the ListBox with filtered results
            PatientsListBox.ItemsSource = null; // Force refresh
            PatientsListBox.ItemsSource = patients;
        }

        private void SearchTextBox_GotFocus(object sender, RoutedEventArgs e)
        {
            // Clear the default text when the user clicks on the search box
            if (SearchTextBox.Text == "חפש לפי שם או תעודת זהות")
            {
                SearchTextBox.Text = "";
                SearchTextBox.Foreground = System.Windows.Media.Brushes.Black;
            }
        }

        private void DownloadPdf_Click(object sender, RoutedEventArgs e)
        {
            if (sender is Button button && button.CommandParameter is string pdfFilePath)
            {
                try
                {
                    Process.Start(new ProcessStartInfo(pdfFilePath) { UseShellExecute = true });
                }
                catch (Exception ex)
                {
                    MessageBox.Show($"לא ניתן לפתוח קובץ PDF: {ex.Message}");
                }
            }
        }

        private void DownloadDoc_Click(object sender, RoutedEventArgs e)
        {
            if (sender is Button button && button.CommandParameter is string docFilePath)
            {
                try
                {
                    Process.Start(new ProcessStartInfo(docFilePath) { UseShellExecute = true });
                }
                catch (Exception ex)
                {
                    MessageBox.Show($"לא ניתן לפתוח קובץ DOCX: {ex.Message}");
                }
            }
        }
    }
}