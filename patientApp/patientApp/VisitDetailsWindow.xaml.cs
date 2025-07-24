using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.IO;
using System.Linq;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Input;

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
            
            // Load files into their respective lists
            UpdateFileLists();
            
            // Handle double-click to open files
            PdfFilesListBox.MouseDoubleClick += FileList_MouseDoubleClick;
            DocFilesListBox.MouseDoubleClick += FileList_MouseDoubleClick;
        }

        private void UpdateFileLists()
        {
            // Update PDF files list
            PdfFilesListBox.ItemsSource = null;
            PdfFilesListBox.ItemsSource = _visit.PdfFiles.ToList();
            
            // Update DOCX files list
            DocFilesListBox.ItemsSource = null;
            DocFilesListBox.ItemsSource = _visit.DocFiles.ToList();
            
            // Update tab headers with counts
            if (PdfFilesListBox.TemplatedParent is TabItem pdfTab)
            {
                pdfTab.Header = $"PDF ({_visit.PdfFiles.Count()})";
            }
            
            if (DocFilesListBox.TemplatedParent is TabItem docTab)
            {
                docTab.Header = $"DOCX ({_visit.DocFiles.Count()})";
            }
        }
        
        private void FileList_MouseDoubleClick(object sender, MouseButtonEventArgs e)
        {
            if (sender is ListBox listBox && listBox.SelectedItem is FileAttachment file)
            {
                OpenFile(file);
            }
        }

        private void OpenFile_Click(object sender, RoutedEventArgs e)
        {
            if (sender is Button button && button.CommandParameter is FileAttachment file)
            {
                OpenFile(file);
            }
        }

        private void OpenFile(FileAttachment file)
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
                MessageBox.Show($"אירעה שגיאה בעת פתיחת הקובץ: {ex.Message}", "שגיאה", 
                    MessageBoxButton.OK, MessageBoxImage.Error);
            }
        }
    }
}
