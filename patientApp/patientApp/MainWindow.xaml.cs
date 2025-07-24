using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.IO;
using System.Windows;
using System.Windows.Controls;
using patientApp;
using patientApp.Extensions;

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
            
            // Set default row details visibility to collapsed
            VisitsDataGrid.RowDetailsVisibilityMode = DataGridRowDetailsVisibilityMode.Collapsed;
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
            if (e.OriginalSource is FrameworkElement element && element.DataContext is Visit)
            {
                var row = element.ParentOfType<DataGridRow>();
                if (row != null)
                {
                    row.DetailsVisibility = row.DetailsVisibility == Visibility.Visible 
                        ? Visibility.Collapsed 
                        : Visibility.Visible;
                }
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
                            Description = "בדיקה רפואית"
                        }.WithFiles(
                            ("C:/Files/JohnDoe_AnnualCheckup.pdf", "דו'ח שנתי"),
                            ("C:/Files/JohnDoe_LabResults.pdf", "תוצאות מעבדה"),
                            ("C:/Files/JohnDoe_AnnualCheckup.docx", "סיכום ביקור"),
                            ("C:/Files/JohnDoe_MedicalHistory.docx", "היסטוריה רפואית")
                        ),
                        new Visit {
                            VisitDate = DateTime.Now.AddDays(-5),
                            Description = "ייעוץ ראשוני"
                        }.WithFiles(
                            ("C:/Files/JohnDoe_Consultation.pdf", "סיכום ייעוץ"),
                            ("C:/Files/JohnDoe_Consultation.docx", "היסטוריה רפואית")
                        ),
                        new Visit {
                            VisitDate = DateTime.Now.AddDays(-2),
                            Description = "מעקב"
                        }.WithFiles(
                            ("C:/Files/JohnDoe_FollowUp.pdf", "סיכום מעקב"),
                            ("C:/Files/JohnDoe_FollowUp.docx", "סיכום מעקב")
                        )
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
                            Description = "ייעוץ רפואי"
                        }.WithFiles(
                            ("C:/Files/JaneSmith_Consultation.pdf", "סיכום ייעוץ"),
                            ("C:/Files/JaneSmith_Consultation.docx", "הערות נוספות")
                        ),
                        new Visit {
                            VisitDate = DateTime.Now.AddDays(-15),
                            Description = "בדיקת דם"
                        }.WithFiles(
                            ("C:/Files/JaneSmith_BloodTest.pdf", "תוצאות בדיקות דם"),
                            ("C:/Files/JaneSmith_BloodTest.docx", "הערות רופא")
                        ),
                        new Visit {
                            VisitDate = DateTime.Now.AddDays(-11),
                            Description = "הפניה רפואית",
                        }.WithFiles(
                            ("C:/Files/JaneSmith_Consultation.pdf", "סיכום הפנייה רפואית"),
                            ("C:/Files/JaneSmith_Consultation.docx", "הערות רופא")
                        )
                    }
                },
                new Patient
                {
                    Name = "אדם מחסובק",
                    Id = "321123123",
                    Visits = new List<Visit>
                    {
                        new Visit {
                            VisitDate = DateTime.Now.AddDays(-40),
                            Description = "בדיקה רפואית",
                        }.WithFiles(
                            ("C:/Files/JaneSmith_Consultation.pdf", "סיכום הפנייה רפואית"),
                            ("C:/Files/JaneSmith_Consultation.docx", "הערות רופא")
                        ),
                        new Visit {
                            VisitDate = DateTime.Now.AddDays(-20),
                            Description = "טיפול רפואי",
                        }.WithFiles(
                            ("C:/Files/JaneSmith_Consultation.pdf", "סיכום הפנייה רפואית"),
                            ("C:/Files/JaneSmith_Consultation.docx", "הערות רופא")
                        ),
                        new Visit {
                            VisitDate = DateTime.Now.AddDays(-11),
                            Description = "הפניה רפואית",
                        }.WithFiles(
                            ("C:/Files/JaneSmith_Consultation.pdf", "סיכום הפנייה רפואית"),
                            ("C:/Files/JaneSmith_Consultation.docx", "הערות רופא")
                        )
                    }
                }
            };
            
            // Initialize patients list with all patients (no duplicates)
            patients.Clear();
            patients.AddRange(samplePatients);
        }

        private void PatientsListBox_SelectionChanged(object sender, SelectionChangedEventArgs e)
        {
            var selectedPatient = PatientsListBox.SelectedItem as Patient;
            if (selectedPatient != null)
            {
                VisitsDataGrid.ItemsSource = selectedPatient.Visits;
                // Collapse all row details when changing patients
                foreach (var item in VisitsDataGrid.Items)
                {
                    var row = VisitsDataGrid.ItemContainerGenerator.ContainerFromItem(item) as DataGridRow;
                    if (row != null)
                    {
                        row.DetailsVisibility = Visibility.Collapsed;
                    }
                }
            }
            else
            {
                VisitsDataGrid.ItemsSource = null;
            }
        }

        private DateTime? lastSearchDate = null;
        private string lastSearchText = string.Empty;

        private void UpdatePatientList()
        {
            if (PatientsListBox == null) return;

            string searchText = SearchTextBox.Text.Trim();
            if (searchText == "חפש לפי שם או תעודת זהות")
            {
                searchText = string.Empty;
            }

            var query = allPatients.AsEnumerable();

            // Apply text filter if search text is provided
            if (!string.IsNullOrWhiteSpace(searchText))
            {
                searchText = searchText.ToLower();
                query = query.Where(p => (p.Name?.ToLower().Contains(searchText) ?? false) ||
                                       (p.Id?.Contains(searchText, StringComparison.OrdinalIgnoreCase) ?? false));
            }

            // Apply date filter if a date is selected
            if (lastSearchDate.HasValue)
            {
                var targetDate = lastSearchDate.Value.Date;
                query = query.Where(p => p.Visits?.Any(v => v.VisitDate.Date == targetDate) ?? false);
            }

            // Get distinct patients by ID
            var filteredPatients = query
                .GroupBy(p => p.Id)
                .Select(g => g.First())
                .ToList();

            // Update the patients collection
            patients.Clear();
            foreach (var patient in filteredPatients)
            {
                patients.Add(patient);
            }

            // Update the UI
            PatientsListBox.ItemsSource = null; // Force refresh
            PatientsListBox.ItemsSource = patients;
            
            // Select first item if available
            if (PatientsListBox.Items.Count > 0)
            {
                PatientsListBox.SelectedIndex = 0;
            }
        }

        private void SearchTextBox_TextChanged(object sender, TextChangedEventArgs e)
        {
            if (SearchTextBox == null) return;
            lastSearchText = SearchTextBox.Text.Trim();
            UpdatePatientList();
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

        private void VisitDatePicker_SelectedDateChanged(object sender, SelectionChangedEventArgs e)
        {
            lastSearchDate = VisitDatePicker.SelectedDate?.Date;
            UpdatePatientList();
        }

        private void ClearDateButton_Click(object sender, RoutedEventArgs e)
        {
            VisitDatePicker.SelectedDate = null;
            lastSearchDate = null;
            UpdatePatientList();
        }

        private void DownloadPdf_Click(object sender, RoutedEventArgs e)
        {
            if (sender is Button button && button.DataContext is Visit visit)
            {
                OpenFiles(visit.PdfFilePaths);
            }
        }

        private void OpenFiles(IEnumerable<string> filePaths)
        {
            foreach (var filePath in filePaths)
            {
                if (!string.IsNullOrEmpty(filePath) && System.IO.File.Exists(filePath))
                {
                    Process.Start(new ProcessStartInfo(filePath) { UseShellExecute = true });
                }
            }
        }

        private void DownloadFile_Click(object sender, RoutedEventArgs e)
        {
            if (sender is Button button && button.DataContext is FileAttachment file)
            {
                if (file == null || string.IsNullOrEmpty(file.FilePath))
                {
                    MessageBox.Show("לא ניתן לפתוח קובץ: נתיב הקובץ חסר או לא תקין", "שגיאה", MessageBoxButton.OK, MessageBoxImage.Error);
                    return;
                }

                try
                {
                    if (File.Exists(file.FilePath))
                    {
                        Process.Start(new ProcessStartInfo(file.FilePath) { UseShellExecute = true });
                    }
                    else
                    {
                        var result = MessageBox.Show("הקובץ לא נמצא. האם ברצונך לחפש אותו?", "קובץ לא נמצא", 
                            MessageBoxButton.YesNo, MessageBoxImage.Question);
                        
                        if (result == MessageBoxResult.Yes)
                        {
                            var openFileDialog = new Microsoft.Win32.OpenFileDialog
                            {
                                Title = "אתר את הקובץ",
                                Filter = $"{file.FileType} files|*{Path.GetExtension(file.FilePath)}|All files|*.*",
                                FileName = Path.GetFileName(file.FilePath)
                            };

                            if (openFileDialog.ShowDialog() == true)
                            {
                                // Update the file path if user selects a new one
                                file.FilePath = openFileDialog.FileName;
                                Process.Start(new ProcessStartInfo(file.FilePath) { UseShellExecute = true });
                            }
                        }
                    }
                }
                catch (Exception ex)
                {
                    MessageBox.Show($"שגיאה בפתיחה של הקובץ: {ex.Message}", "שגיאה", MessageBoxButton.OK, MessageBoxImage.Error);
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