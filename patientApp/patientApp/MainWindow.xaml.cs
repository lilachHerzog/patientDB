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
        private List<Patient> patients;

        public MainWindow()
        {
            InitializeComponent();
            LoadPatients();
            PatientsListBox.ItemsSource = patients;
        }

        private void LoadPatients()
        {
            patients = new List<Patient>
            {
                new Patient
                {
                    Name = "ג'ון דוא",
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
                    Visits = new List<Visit>
                    {
                        new Visit {
                            VisitDate = DateTime.Now.AddDays(-20),
                            Description = "ייעוץ רפואית",
                            PdfFilePath = "C:/Files/JaneSmith_Consultation.pdf",
                            DocFilePath = "C:/Files/JaneSmith_Consultation.docx"
                        }
                    }
                }
            };
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
                    MessageBox.Show($"Could not open PDF file: {ex.Message}");
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
                    MessageBox.Show($"Could not open DOC file: {ex.Message}");
                }
            }
        }
    }
}