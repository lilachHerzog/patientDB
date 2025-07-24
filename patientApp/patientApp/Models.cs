using System.IO;

namespace patientApp
{
    public static class VisitExtensions
    {
        public static Visit WithFiles(this Visit visit, params (string filePath, string? displayName)[] files)
        {
            if (visit == null) throw new ArgumentNullException(nameof(visit));
            
            foreach (var (filePath, displayName) in files)
            {
                if (!string.IsNullOrEmpty(filePath))
                {
                    visit.AddAttachment(filePath, displayName);
                }
            }
            
            return visit;
        }
    }

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

    public class FileAttachment
    {
        public string DisplayName { get; set; } = string.Empty;
        public string FilePath { get; set; } = string.Empty;
        public string FileType => Path.GetExtension(FilePath)?.ToLower() == ".pdf" ? "PDF" : "DOC";

        public FileAttachment(string filePath, string? displayName = null)
        {
            FilePath = filePath;
            DisplayName = displayName ?? Path.GetFileName(filePath);
        }
    }

    public class Visit
    {
        public DateTime VisitDate { get; set; }
        public string Description { get; set; } = string.Empty;
        public List<FileAttachment> Attachments { get; set; } = new List<FileAttachment>();
        public string Status { get; set; } = string.Empty;

        // Helper properties for easy access to file paths
        public IEnumerable<string> PdfFilePaths => Attachments.Where(f => f.FileType == "PDF").Select(f => f.FilePath);
        public IEnumerable<string> DocFilePaths => Attachments.Where(f => f.FileType == "DOC").Select(f => f.FilePath);

        // For backward compatibility
        [Obsolete("Use AddAttachment or Attachments property instead")]
        public string? PdfFilePath { get => PdfFiles.FirstOrDefault()?.FilePath; set { if (value != null) AddAttachment(value); } }
        
        [Obsolete("Use AddAttachment or Attachments property instead")]
        public string? DocFilePath { get => DocFiles.FirstOrDefault()?.FilePath; set { if (value != null) AddAttachment(value); } }

        public IEnumerable<FileAttachment> PdfFiles => Attachments.Where(f => f.FileType == "PDF");
        public IEnumerable<FileAttachment> DocFiles => Attachments.Where(f => f.FileType == "DOC");

        public void AddAttachment(string filePath, string? displayName = null)
        {
            if (File.Exists(filePath))
            {
                Attachments.Add(new FileAttachment(filePath, displayName));
            }
            else
            {
                // For backward compatibility with hardcoded sample data
                Attachments.Add(new FileAttachment(filePath, displayName ?? Path.GetFileName(filePath)));
            }
        }
    }
}
